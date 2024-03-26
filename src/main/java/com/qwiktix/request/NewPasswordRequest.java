package com.qwiktix.request;


import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class NewPasswordRequest {
    private String mail;
    private String password;
}
