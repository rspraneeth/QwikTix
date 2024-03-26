package com.qwiktix.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordToken {

    @Id
    @SequenceGenerator(sequenceName = "token_sequence",name = "token_sequence",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "token_sequence")
    private Integer id;
    private Integer otp;
    private LocalDateTime expiry;
    @OneToOne
    private User user;
}
