package com.toDoApp.util;

import com.toDoApp.data.model.User;
import com.toDoApp.data.repository.UserRepository;
import com.toDoApp.dto.request.RegisterRequest;
import com.toDoApp.dto.request.LoginRequest;
import com.toDoApp.exception.*;

import java.util.Map;
import java.util.Optional;

public class UserValidator {

    public static void validateRegister(RegisterRequest request, UserRepository userRepository) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordMismatchException("Password and confirmation password do not match");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsernameAlreadyExistsException("Email '" + request.getEmail() + "' is already registered");
        }
    }

    public static User validateLogin(LoginRequest request, UserRepository userRepository) {
        Optional<User> optionUser = userRepository.findByUsername(request.getUsername());
        if (optionUser.isEmpty()) {
            throw new UserNotFoundException("Username '" + request.getUsername() + "' not found");
        }
        return optionUser.get();
    }
    public static void validatePasswordMatch(String newPassword, String confirmNewPassword) {
        if (!newPassword.equals(confirmNewPassword)) {
            throw new PasswordMismatchException("New password and confirmation do not match");
        }
    }
    public static void validateUserExists(String email, UserRepository userRepository) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Email '" + email + "' not found in our system");
        }
    }
    public static void validateTokenExists(String token, Map<String, String> resetTokens) {
        if (!resetTokens.containsKey(token)) {
            throw new TokenNotFoundException("Password reset token is invalid or has expired");
        }
    }
}