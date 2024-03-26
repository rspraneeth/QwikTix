package com.qwiktix.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qwiktix.data.BaseUserPrincipal;
import com.qwiktix.data.User;
import com.qwiktix.repository.UserRepository;
import com.qwiktix.response.AdminUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username){
        System.out.println("for "+username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new BaseUserPrincipal(user);
    }

    public String registerNewUser(User user) throws JsonProcessingException {
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user));
        try{
            userRepository.save(user);
            return "success";
        }catch (Exception e){
            return "failed";
        }
    }

    public AdminUserResponse getUserResponse(){
        try {
            return new AdminUserResponse(userRepository.findAll(),(int)userRepository.count());
        }catch (Exception e){
            return new AdminUserResponse(new ArrayList<>(),0);
        }
    }

    public String findUserByName(String name) {
        Optional<User> optionalUser = userRepository.findByName(name);

        if (optionalUser.isPresent()){
            return "exist";
        }

        return "na";

    }

    public void changePassword(String username, String password) throws JsonProcessingException {
        System.out.println("for "+username+", password to set is "+password);
        User user = userRepository.findByUsername(username);
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user));
        System.out.println();
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user));
    }
}
