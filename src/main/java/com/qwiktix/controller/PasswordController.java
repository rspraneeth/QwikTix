package com.qwiktix.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qwiktix.request.NewPasswordRequest;
import com.qwiktix.service.PasswordService;
import com.qwiktix.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/forgot-password")
    public String forgotPassword(){
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@RequestParam String mail, RedirectAttributes redirectAttributes, HttpSession session){
        System.out.println("in post forgot with mail "+mail);
        if(passwordService.validateUser(mail)){
            session.setAttribute("mail", mail);
            redirectAttributes.addFlashAttribute("successMessage", "Otp sent to registered mail id, please enter it here.");
            return "redirect:/user/otp";
        }else {
            redirectAttributes.addFlashAttribute("errorMessage", "User with entered mail id doesnt exist");
            return "redirect:/user/forgot-password";
        }
    }

    @GetMapping("/user/otp")
    public String otpEnter(){
        return "enter-otp";
    }

    @PostMapping("/user/validate/otp")
    public String validateOtp(@RequestParam Integer otp, RedirectAttributes redirectAttributes, HttpSession session){
        if (otp.toString().length() != 6){
            redirectAttributes.addFlashAttribute("errorMessage", "Otp is 6 digits, please check your mail and try again");
            return "redirect:/user/otp";
        }
        String mail = (String) session.getAttribute("mail");
        System.out.println("in post otp validation "+otp+" mail is "+mail);
        if(passwordService.validateOtp(otp, mail)){
            redirectAttributes.addFlashAttribute("successMessage", "Valid Otp, enter new password");
            System.out.println("on validate otp post");
            return "redirect:/user/new-password";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Otp expired/invalid, try again");
        return "redirect:/user/forgot-password";
    }

    @GetMapping("/user/new-password")
    public String changePassword(Model model, HttpSession session){
        NewPasswordRequest newPasswordRequest;
        newPasswordRequest = new NewPasswordRequest();
        newPasswordRequest.setMail((String) session.getAttribute("mail"));
        model.addAttribute("newPasswordRequest", newPasswordRequest);
        return "change-password";
    }

    @PostMapping("/user/new-password")
    public String newPassword(@ModelAttribute NewPasswordRequest newPasswordRequest, RedirectAttributes redirectAttributes, HttpSession session){
        newPasswordRequest.setMail((String) session.getAttribute("mail"));
        if (newPasswordRequest.getPassword().length()<6){
            redirectAttributes.addFlashAttribute("errorMessage", "Min Password length is 6 characters");
            return "redirect:/user/new-password";
        }
        System.out.println("checking....changing password for mail"+newPasswordRequest.getMail());
        try {
            userService.changePassword(newPasswordRequest.getMail(), newPasswordRequest.getPassword());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        redirectAttributes.addFlashAttribute("successMessage", "updated your password, please use new password to login");
        return "redirect:/login";
    }
}
