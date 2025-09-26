package com.toDoApp.service.user;

import com.toDoApp.data.model.User;
import com.toDoApp.data.repository.UserRepository;
import com.toDoApp.dto.request.RegisterRequest;
import com.toDoApp.dto.request.LoginRequest;
import com.toDoApp.dto.response.AuthResponse;
import com.toDoApp.exception.PasswordMismatchException;
import com.toDoApp.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }


    @Test
    public void testRegisterAndLogin() {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("abdulrasaq");
        register.setEmail("rasaq@example.com");
        register.setFirstName("rasaq");
        register.setLastName("abdul");
        register.setPassword("password123");
        register.setConfirmPassword("password123");

        AuthResponse response = userService.register(register);
        assertNotNull(response.getId());

        LoginRequest login = new LoginRequest("abdulrasaq", "password123");
        AuthResponse loginResponse = userService.login(login);
        assertEquals("abdulrasaq",
                loginResponse.getUsername());
    }

    @Test
    public void testLoginFailsWithWrongPassword() {
        RegisterRequest register = new RegisterRequest(
                "rasaq10",
                "rasaq@example.com",
                "rasaq",
                "abdul",
                "secret123",
                "secret123"
        );
        userService.register(register);
        LoginRequest login = new LoginRequest("rasaq10", "password");
        assertThrows(PasswordMismatchException.class, () -> userService.login(login));
    }
    @Test
    public void testLoginFailsForUnknownUser() {
        LoginRequest login = new LoginRequest("unknownUser", "password");
        assertThrows(UserNotFoundException.class, () -> userService.login(login));
    }
}
