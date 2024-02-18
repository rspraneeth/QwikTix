package com.qwiktix.config;

import com.qwiktix.data.User;
import com.qwiktix.enums.Role;
import com.qwiktix.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DatabaseSeeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedDatabase(){
        return args -> {
            User user1 = new User("NEW ADMIN","admin@gmail.com","admin@gmail.com",passwordEncoder.encode("123456"),"12/12/1999", Role.ADMIN);
            User user2 = new User("NEW USER","user@gmail.com","user@gmail.com", passwordEncoder.encode("123456"), "12/10/1999", Role.USER);
            if(userRepository.count()==0) {
                userRepository.saveAll(List.of(user1, user2));
            }
        };
    }
}
