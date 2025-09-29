package com.toDoApp.controller;

import com.toDoApp.dto.request.*;
import com.toDoApp.dto.response.AuthResponse;
import com.toDoApp.exception.PasswordMismatchException;
import com.toDoApp.exception.TokenNotFoundException;
import com.toDoApp.exception.UserNotFoundException;
import com.toDoApp.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Return the specific error message from the service
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // Specific: Username not found
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        } catch (PasswordMismatchException e) {
            // Specific: Wrong password
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            String response = userService.forgotPassword(request);
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException e) {
            // Specific: Email not found
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Failed to process password reset request. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            String response = userService.resetPassword(request);
            return ResponseEntity.ok(response);
        } catch (TokenNotFoundException e) {
            // Specific: Invalid or expired token
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        } catch (UserNotFoundException e) {
            // Specific: User not found
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
        } catch (PasswordMismatchException e) {
            // Specific: Password validation failed
            List<String> errors = new ArrayList<>();
            errors.add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        } catch (Exception e) {
            List<String> errors = new ArrayList<>();
            errors.add("Failed to reset password. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            errors.add(error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}