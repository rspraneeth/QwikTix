package com.qwiktix.data;

import com.qwiktix.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class User{
    @Id
    @SequenceGenerator(sequenceName = "user_sequence",name = "user_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    private long Id;
    @NotBlank(message = "Name is required")
    @Column(unique = true)
    private String name;
    @NotBlank(message = "Name is required")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "Name is required")
    private String password;
    @Column(unique = true)
    private String email;
    private String dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isDeleted=false;

    public User(String name,String username, String email, String password, String dateOfBirth, Role role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }
}
