package com.qwiktix.controller;

import com.qwiktix.data.User;
import com.qwiktix.enums.Role;
import com.qwiktix.helpers.ValidationHelper;
import com.qwiktix.request.RegistrationRequest;
import com.qwiktix.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.Period;

@Controller
@Tag(name = "User Api", description = "Api end point for user registration")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ValidationHelper validationHelper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "POST operation", description = "End point registers new users.")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        User user = new User(
                registrationRequest.getName(),
                registrationRequest.getEmail(),
                registrationRequest.getEmail(),
                passwordEncoder.encode(registrationRequest.getPassword()),
                registrationRequest.getDob(),
                Role.USER
        );
        if (!validationHelper.isValidRegistrationData(user)){
            redirectAttributes.addFlashAttribute("errorMessage", "Data is Invalid, enter valid data!");
            return "redirect:/register";
        }

        if (registrationRequest.getPassword().length()<6){
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 5 characters!");
            return "redirect:/register";
        }

        String name = registrationRequest.getName();
        if("exist".equalsIgnoreCase(userService.findUserByName(name))){
            redirectAttributes.addFlashAttribute("errorMessage", "User with same name already exist! please use another name.");
            return "redirect:/register";
        }

        LocalDate dob = LocalDate.parse(registrationRequest.getDob());
        LocalDate currentDate = LocalDate.now();
        int age = Period.between(dob, currentDate).getYears();
        if (age<18){
            redirectAttributes.addFlashAttribute("errorMessage", "You must be 18 years old or more!");
            return "redirect:/register";
        }

        try {
            String result = userService.registerNewUser(user);
            if (result.equalsIgnoreCase("success")) {
                redirectAttributes.addFlashAttribute("successMessage", "Account Created Successfully! Login to proceed");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Failed to register.Please try again");
                return "redirect:/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to register.Please try again");
            return "redirect:/register";
        }
    }

}
