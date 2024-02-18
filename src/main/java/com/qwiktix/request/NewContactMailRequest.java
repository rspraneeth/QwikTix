package com.qwiktix.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewContactMailRequest {
    private String name;
    private String email;
    private String subject;
    private String message;
}
