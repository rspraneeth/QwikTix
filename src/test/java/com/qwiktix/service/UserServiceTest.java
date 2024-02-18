package com.qwiktix.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qwiktix.data.User;
import com.qwiktix.enums.Role;
import com.qwiktix.repository.UserRepository;
import com.qwiktix.response.AdminUserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLoadUserByUsername() {

        String username = "test";
        User mockUser = new User("test", username, username,username,username, Role.USER);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(mockUser);

        UserDetails userDetails = userService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertFalse(userDetails.getPassword().isEmpty());
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        String username = "not.found.user";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void testRegisterNewUserSuccess() throws JsonProcessingException {
        User newUser = new User("New User","newUser","newUser@gmail.com","123456" ,"12/12/1995", Role.USER);
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        String result = userService.registerNewUser(newUser);

        assertEquals("success", result);
    }

    @Test
    public void testRegisterNewUserFailure() throws JsonProcessingException {
        User newUser = new User("New User","newUser","newUser@gmail.com","123456" ,"12/12/1995", Role.USER);
        Mockito.when(userRepository.save(newUser)).thenThrow(new RuntimeException("Error saving user"));

        String result = userService.registerNewUser(newUser);

        assertEquals("failed", result);
    }

    @Test
    public void testGetUserResponse() {
        User user1 = new User("New User1","newUser1","newUser1@gmail.com","123456" ,"12/12/1995", Role.USER);
        User user2 = new User("New User2","newUser2","newUser2@gmail.com","123456" ,"12/12/1995", Role.USER);
        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        Mockito.when(userRepository.findAll()).thenReturn(userList);
        Mockito.when(userRepository.count()).thenReturn(2L);

        AdminUserResponse adminUserResponse = userService.getUserResponse();

        assertNotNull(adminUserResponse);
        assertEquals(userList, adminUserResponse.getUsers());
        assertEquals(2, adminUserResponse.getUsers().size());
    }

    @Test
    public void testGetUserResponseException() {
        Mockito.when(userRepository.findAll()).thenThrow(new RuntimeException("Error fetching users"));

        AdminUserResponse adminUserResponse = userService.getUserResponse();

        assertNotNull(adminUserResponse);
        assertTrue(adminUserResponse.getUsers().isEmpty());
        assertEquals(0, adminUserResponse.getUsers().size());
    }
}