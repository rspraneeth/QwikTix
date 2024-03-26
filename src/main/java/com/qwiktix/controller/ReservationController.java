package com.qwiktix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwiktix.data.Event;
import com.qwiktix.data.Reservation;
import com.qwiktix.data.User;
import com.qwiktix.helpers.ValidationHelper;
import com.qwiktix.repository.EventRepository;
import com.qwiktix.repository.UserRepository;
import com.qwiktix.request.NewReservationRequest;
import com.qwiktix.request.UpdateEventRequest;
import com.qwiktix.request.UpdateReservationRequest;
import com.qwiktix.service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ValidationHelper validationHelper;

    @GetMapping("/admin/reservations/add")
    public String admin_add_reservation(Model model,HttpSession httpSession) {
        User user = (User)httpSession.getAttribute("user");
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("events",eventRepository.findFutureEvents());
        return "admin_add_reservation";
    }

    @PostMapping("/admin/reservations/store")
    public String addNewReservation(@ModelAttribute("newReservationRequest") NewReservationRequest newReservationRequest, RedirectAttributes redirectAttributes,HttpSession httpSession){

        if (!validationHelper.isValidNewReservationData(newReservationRequest)){
            redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data");
            return "redirect:/admin/reservations/add";
        }
        try {
            User user = userRepository.findById(Long.valueOf(newReservationRequest.getUserId())).orElse(null);
            Event event = eventRepository.findById(Long.valueOf(newReservationRequest.getEventId())).orElse(null);
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user));
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(event));
            if (user==null || event==null){
                redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data");
                return "redirect:/admin/reservations/add";
            }
            Reservation reservation = new Reservation(
                    user,
                    event,
                    newReservationRequest.getNumberOfTickets(),
                    newReservationRequest.getReservationTime()
            );
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(reservation));
            String result = reservationService.addNewReservation(reservation);
            if (result.equalsIgnoreCase("success")) {
                redirectAttributes.addFlashAttribute("successMessage", "Reservation added successfully");
                User us = (User) httpSession.getAttribute("user");
                if (us.getRole().toString().equalsIgnoreCase("ADMIN")){
                    return "redirect:/admin/reservations";
                }else {
                    return "redirect:/user/reservations";
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to create reservation.Please try again");
                return "redirect:/admin/reservations/add";
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create reservation.Please try again");
            return "redirect:/admin/reservations/add";
        }
    }


    @GetMapping("/user/reservations/add")
    public String user_add_reservation(Model model,HttpSession httpSession) {
        User user = (User)httpSession.getAttribute("user");
        model.addAttribute("users",userRepository.findAll());
        model.addAttribute("events",eventRepository.findFutureEvents());
        return "user_add_reservation";
    }

    @PostMapping("/user/reservations/store")
    public String user_post_reservation(@ModelAttribute("newReservationRequest") NewReservationRequest newReservationRequest, RedirectAttributes redirectAttributes,HttpSession httpSession){
        if (!validationHelper.isValidNewReservationData(newReservationRequest)){
            redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data");
            return "redirect:/user/events/show/book/"+newReservationRequest.getEventId();
        }
        if (newReservationRequest.getNumberOfTickets() <= 0){
            redirectAttributes.addFlashAttribute("errorMessage", "No of tickets should be more than 0");
            return "redirect:/user/events/show/book/"+newReservationRequest.getEventId();
        }
        try {
            User user = userRepository.findById(Long.valueOf(newReservationRequest.getUserId())).orElse(null);
            Event event = eventRepository.findById(Long.valueOf(newReservationRequest.getEventId())).orElse(null);
            if (user==null || event==null){
                redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data");
                return "redirect:/user/events/show/book/"+newReservationRequest.getEventId();
            }
            Reservation reservation = new Reservation(
                    user,
                    event,
                    newReservationRequest.getNumberOfTickets(),
                    newReservationRequest.getReservationTime()
            );
            String result = reservationService.addNewReservation(reservation);
            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(reservation));
            if (result.equalsIgnoreCase("success")) {
                redirectAttributes.addFlashAttribute("successMessage", "Reservation added successfully");
                return "redirect:/user/reservations";
            }else{
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to create reservation.Please try again");
                return "redirect:/";
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create reservation.Please try again");
            return "redirect:/user/events/show/book/"+newReservationRequest.getEventId();
        }
    }

    @GetMapping("/user/reservations")
    public String user_reservations(Model model, HttpSession httpSession) {
        User user = (User)httpSession.getAttribute("user");
        model.addAttribute("userReservationsResponse",reservationService.getUserFutureReservations(user.getId()));
        model.addAttribute("userPastReservationsResponse",reservationService.getUserPastReservations(user.getId()));
        return "user_all_reservations";
    }

    @GetMapping("/admin/reservations")
    public String admin_reservations(Model model,HttpSession httpSession) {
        User user = (User)httpSession.getAttribute("user");
        model.addAttribute("adminReservationResponse",reservationService.adminReservationsFuture());
        model.addAttribute("adminPastReservationResponse",reservationService.adminReservationsPast());
        return "admin_all_reservations";
    }

    @GetMapping("/reservations/{id}")
    public String viewReservation(@PathVariable int id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            model.addAttribute("viewReservation", reservationService.viewReservation(id));
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "admin_view_reservation";
            } else {
                return "user_view_reservation";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/user/events";
            }
        }
    }

    @GetMapping("/reservations/edit/{id}")
    public String editReservation(@PathVariable int id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            model.addAttribute("editReservation", reservationService.editReservation(id));
            model.addAttribute("users",userRepository.findAll());
            model.addAttribute("events",eventRepository.findAll());
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "admin_edit_reservation";
            } else {
                return "redirect:/user/events";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/user/events";
            }
        }
    }

    @PostMapping("/admin/reservations/update")
    public String updateReservation(@ModelAttribute("updateReservationRequest") UpdateReservationRequest updateReservationRequest, Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        try {
            reservationService.updateReservation(updateReservationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Reservation updated successfully");
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/user/events";
            }
        }
    }

    @GetMapping("/reservations/delete/{id}")
    public String deleteReservation(@PathVariable int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            reservationService.deleteReservation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Reservation deleted successfully");
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/user/events";
            }
        }
    }

    @GetMapping("/reservations/cancel/{id}")
    public String cancelReservation(@PathVariable int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("successMessage", "Reservation cancelled successfully");
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/";
            } else {
                return "redirect:/";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/user/events";
            }
        }
    }
}
