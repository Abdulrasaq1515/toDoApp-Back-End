package com.toDoApp.service.user;

import com.toDoApp.data.model.User;
import com.toDoApp.data.repository.UserRepository;
import com.toDoApp.dto.request.*;
import com.toDoApp.dto.response.AuthResponse;
import com.toDoApp.exception.PasswordMismatchException;
import com.toDoApp.exception.UserNotFoundException;
import com.toDoApp.util.UserMapper;
import com.toDoApp.util.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Map<String, String> resetTokens = new HashMap<>();

    @Override
    public AuthResponse register(RegisterRequest request) {
        UserValidator.validateRegister(request, userRepository);
        User user = UserMapper.toUser(request, passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        return UserMapper.toAuthResponse(saved);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = UserValidator.validateLogin(request, userRepository);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Incorrect password for username '" + request.getUsername() + "'");
        }
        return UserMapper.toAuthResponse(user);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        UserValidator.validateUserExists(request.getEmail(), userRepository);
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, request.getEmail());
        return "Password reset token have been sent";
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        UserValidator.validatePasswordMatch(request.getNewPassword().trim(), request.getConfirmNewPassword().trim());
        UserValidator.validateTokenExists(request.getToken(), resetTokens);
        String email = resetTokens.get(request.getToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User account not found"));
        UserMapper.updatePassword(user, passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        resetTokens.remove(request.getToken());
        return "Password has been reset successfully";
    }
}