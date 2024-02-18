package com.qwiktix.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwiktix.data.BaseUserPrincipal;
import com.qwiktix.data.Event;
import com.qwiktix.data.User;
import com.qwiktix.facades.IAuthenticationFacade;
import com.qwiktix.repository.EventRepository;
import com.qwiktix.request.NewContactMailRequest;
import com.qwiktix.request.NewEventRequest;
import com.qwiktix.request.RegistrationRequest;
import com.qwiktix.service.EmailService;
import com.qwiktix.service.EventService;
import com.qwiktix.service.ReservationService;
import com.qwiktix.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RouteController {

    @Autowired
    private IAuthenticationFacade authenticationFacade;
    @Autowired
    private EventService eventService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;

    @Value("${spring.mail.username}")
    private String companyEmail;

    @GetMapping("/")
    public String admin_dashboard(Model model, HttpSession session) {
        try {
            Authentication authentication = authenticationFacade.getAuthentication();
            BaseUserPrincipal baseUserPrincipal = (BaseUserPrincipal) authentication.getPrincipal();
            User user = baseUserPrincipal.getUser();
            if (user.getRole().toString().equalsIgnoreCase("ADMIN")) {
                session.setAttribute("user", user);
                model.addAttribute("user", user);
                model.addAttribute("adminEventResponse",eventService.adminEvent());
                model.addAttribute("adminReservationResponse",reservationService.adminReservations());
                model.addAttribute("adminUserResponse",userService.getUserResponse());
                return "admin_dashboard";
            }else if(user.getRole().toString().equalsIgnoreCase("USER")) {
                session.setAttribute("user", user);
                model.addAttribute("user", user);
                System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(reservationService.getUserReservations(user.getId())));
                model.addAttribute("userEventsResponse",eventService.adminEvent());
                model.addAttribute("userReservationsResponse",reservationService.getUserReservations(user.getId()));
                return "user_dashboard";
            }
            return "login";
        }catch (Exception e){
            return "login";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    @GetMapping("/admin/events/add")
    public String admin_add_event(Model model) {
        return "admin_add_event";
    }

    @GetMapping("/admin/events")
    public String admin_events(Model model) {
        model.addAttribute("adminEventResponse",eventService.adminEvent());
        return "admin_all_events";
    }


    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @PostMapping("/contact/send/mail")
    public String sendContactMail(@ModelAttribute("newContactMailRequest") NewContactMailRequest newContactMailRequest,RedirectAttributes redirectAttributes){
        try {
            emailService.sendEmail(companyEmail,newContactMailRequest.getSubject(),newContactMailRequest.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", "Your message has been received!");
            return "contact";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send message!"+e.getMessage());
            return "contact";
        }
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/user/contact")
    public String user_contact() {
        return "user_contact";
    }

    @PostMapping("/contact/send/mail/user")
    public String sendContactMailUser(@ModelAttribute("newContactMailRequest") NewContactMailRequest newContactMailRequest,RedirectAttributes redirectAttributes){
        try {
            emailService.sendEmail(companyEmail,newContactMailRequest.getSubject(),newContactMailRequest.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", "Your message has been received!");
            return "user_contact";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to send message!"+e.getMessage());
            return "user_contact";
        }
    }

    @GetMapping("/user/about")
    public String user_about() {
        return "user_about";
    }

    @GetMapping("/user/wishlist")
    public String user_wishlist(Model model,HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("wishlistUserResponse",eventService.userWishList(user));
        return "user_wishlist";
    }

    @GetMapping("/user/events/add/wishlist/{eventId}")
    public String addToWishlist(@PathVariable("eventId") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            Event event = eventRepository.findById((long) id).orElse(null);
            eventService.addToWishlist(event,user);
            redirectAttributes.addFlashAttribute("successMessage", "Wishlist updated successfully");
            return "redirect:/";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Item not added!");
            return "redirect:/";
        }
    }

    @GetMapping("/user/events/remove/wishlist/{eventId}")
    public String removeFromWishlist(@PathVariable("eventId") int id, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession){
        try {
            User user = (User) httpSession.getAttribute("user");
            Event event = eventRepository.findById((long) id).orElse(null);
            eventService.removeFromWishlist(event,user);
            redirectAttributes.addFlashAttribute("successMessage", "Wishlist updated successfully");
            return "redirect:/";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("errorMessage", "Item not added!");
            return "redirect:/";
        }
    }
}
