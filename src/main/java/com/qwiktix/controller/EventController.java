package com.qwiktix.controller;

import com.qwiktix.data.Event;
import com.qwiktix.data.User;
import com.qwiktix.helpers.ValidationHelper;
import com.qwiktix.request.NewEventRequest;
import com.qwiktix.request.SearchEventRequest;
import com.qwiktix.request.UpdateEventRequest;
import com.qwiktix.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private ValidationHelper validationHelper;


    @GetMapping("/admin/events/add")
    public String admin_add_event(Model model) {
//        model.addAttribute("newEventRequest", new NewEventRequest());
        return "admin_add_event";
    }

    @GetMapping("/admin/events")
    public String admin_events(Model model) {
        model.addAttribute("adminEventResponse",eventService.adminEvent());
        return "admin_all_events";
    }

    @PostMapping("/admin/events/store")
    public String addNewEvent(@ModelAttribute("newEventRequest") NewEventRequest newEventRequest, RedirectAttributes redirectAttributes, Model model) {
        System.out.println(newEventRequest);
        if (newEventRequest.getTicketPrice().isEmpty() || newEventRequest.getImage().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data");

            redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
            return "redirect:/admin/events/add";
        }

        if (Double.parseDouble(newEventRequest.getTicketPrice()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket price should be more than 0 and must be a number");

            redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
            return "redirect:/admin/events/add";
        }

        MultipartFile imageFile = newEventRequest.getImage();
        long maxImageSize = 2 * 1024 * 1024;
        if (imageFile.getSize() > maxImageSize) {
            redirectAttributes.addFlashAttribute("sizeError", "Image size exceeds the maximum allowed size (2 MB).");
            redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
            return "redirect:/admin/events/add";
        }
        LocalDate eventDate = LocalDate.parse(newEventRequest.getEventDate());
        LocalDate currentDate = LocalDate.now();
        if (eventDate.isBefore(currentDate)){
            redirectAttributes.addFlashAttribute("errorMessage", "Event date must be a future date");
            redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
            return "redirect:/admin/events/add";
        }

        Event event = new Event(
                newEventRequest.getEventName(),
                newEventRequest.getEventDate(),
                newEventRequest.getVenue(),
                newEventRequest.getDescription(),
                Double.parseDouble(newEventRequest.getTicketPrice()),
                newEventRequest.getCategory(),
                newEventRequest.getLocation()
        );
        if (!validationHelper.isValidNewEventData(event)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data");
            redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
            return "redirect:/admin/events/add";
        }
        try {
            String result = eventService.addNewEvent(event, newEventRequest);
            System.out.println(result);
            if (result.equalsIgnoreCase("success")) {
                redirectAttributes.addFlashAttribute("successMessage", "Event added successfully");
                return "redirect:/admin/events";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to create event.Please try again"+result);
                redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
                return "redirect:/admin/events/add";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create event.Please try again"+e.getMessage());
            redirectAttributes.addFlashAttribute("newEventRequest", newEventRequest);
            return "redirect:/admin/events/add";
        }
    }

    @GetMapping("/events/{id}")
    public String viewEvent(@PathVariable int id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            model.addAttribute("viewEvent", eventService.viewEvent(id));
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "admin_view_event";
            } else {
                return "user_view_event";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/user/events";
            }
        }
    }

    @GetMapping("/user/events/show/book/{id}")
    public String show_book_event(@PathVariable int id, Model model, HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        try {
            model.addAttribute("viewEvent", eventService.viewEvent(id));
            return "user_book_event";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/user/events/{id}")
    public String userViewBookEvent(@PathVariable int id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            model.addAttribute("viewEvent", eventService.viewEvent(id));
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "admin_view_event";
            } else {
                return "user_view_event";
            }
        } catch (Exception e) {
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "redirect:/admin/events";
            } else {
                return "redirect:/";
            }
        }
    }

    @GetMapping("/events/edit/{id}")
    public String editEvent(@PathVariable int id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            model.addAttribute("editEvent", eventService.editEvent(id));
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                return "admin_edit_event";
            } else {
                return "redirect:/admin/events";
            }
        } catch (Exception e) {
            return "redirect:/admin/events";
        }
    }

    @PostMapping("/admin/events/update")
    public String updateEvent(@ModelAttribute("updateEventRequest") UpdateEventRequest updateEventRequest, Model model, HttpSession httpSession,RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        LocalDate eventDate = LocalDate.parse(updateEventRequest.getEventDate());
        LocalDate currentDate = LocalDate.now();
        if (eventDate.isBefore(currentDate)){
            redirectAttributes.addFlashAttribute("errorMessage", "Event date must be a future date");
//            redirectAttributes.addFlashAttribute("updateEventRequest", updateEventRequest);
            return "redirect:/events/edit/"+updateEventRequest.getEventId();
        }

        if (Double.parseDouble(updateEventRequest.getTicketPrice()) < 0){
            redirectAttributes.addFlashAttribute("errorMessage", "Ticket price should be more than 0 and must be a number");
            return "redirect:/events/edit/"+updateEventRequest.getEventId();
        }

        try {
            eventService.updateEvent(updateEventRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Event updated successfully");
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                System.out.println("in post admin update role: "+user.getRole());
            }
            return "redirect:/admin/events";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Entered data is invalid, Please check");
            return "redirect:/events/edit/"+updateEventRequest.getEventId();
        }
    }


    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        try {
            eventService.deleteEvent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Event deleted successfully");
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                model.addAttribute("adminEventResponse", eventService.adminEvent());
            }
            return "redirect:/admin/events";
        } catch (Exception e) {
            return "redirect:/admin/events";
        }
    }

    @PostMapping("/admin/dashboard/events/search")
    public String searchAdminEventDashboard(@ModelAttribute("searchEventRequest") SearchEventRequest searchEventRequest,Model model){
        try{
            model.addAttribute("adminEventResponse",eventService.filterEvents(searchEventRequest));
            return "admin_all_events";
        }catch (Exception e){
            return "admin_all_events";
        }
    }

    @PostMapping("/user/dashboard/events/search")
    public String searchUserEventDashboard(@ModelAttribute("searchEventRequest") SearchEventRequest searchEventRequest,Model model){
        try{
            model.addAttribute("userEventsResponse",eventService.filterEvents(searchEventRequest));
            return "user_dashboard";
        }catch (Exception e){
            return "user_dashboard";
        }
    }

}
