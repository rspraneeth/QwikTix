package com.qwiktix.response;

import com.qwiktix.data.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AdminUserResponse {
    private List<User> users;
    private int numberOfUsers;
}
