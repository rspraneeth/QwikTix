package com.qwiktix.service;

import com.qwiktix.data.PasswordToken;
import com.qwiktix.data.User;
import com.qwiktix.repository.PasswordRepository;
import com.qwiktix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordRepository passwordRepository;

    @Autowired
    private EmailService emailService;
    public boolean validateUser(String mail) {
        User user = userRepository.findByUsername(mail);
        if (user == null) {
            return false;
        }
        PasswordToken token = new PasswordToken();
        token.setUser(user);
        token.setExpiry(LocalDateTime.now().plusSeconds(80));
        token.setOtp(new Random().nextInt(100001, 999999));

        PasswordToken tokenCheck = passwordRepository.findByUser(user);
        if (tokenCheck != null){ //checking if there already exists a token for user
            passwordRepository.delete(tokenCheck);
        }
        passwordRepository.save(token);
        emailService.sendEmail(user.getEmail(), "Reset Password", "Hi "+user.getName()+", your otp to reset/change password is "+token.getOtp()+".\nIt expires in next 80 seconds");
        return true;
    }

    public boolean validateOtp(Integer otp, String mail) {
        User user = userRepository.findByUsername(mail);
        PasswordToken token = passwordRepository.findByUser(user);
        System.out.println((Objects.equals(otp, token.getOtp())) && LocalDateTime.now().isBefore(token.getExpiry()));

        return (Objects.equals(otp, token.getOtp())) && LocalDateTime.now().isBefore(token.getExpiry());
    }
}
