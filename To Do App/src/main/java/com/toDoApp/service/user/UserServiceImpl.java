package com.toDoApp.service.user;

import com.toDoApp.data.model.User;
import com.toDoApp.data.repository.UserRepository;
import com.toDoApp.dto.request.*;
import com.toDoApp.dto.response.AuthResponse;
import com.toDoApp.exception.*;
import com.toDoApp.util.UserMapper;
import com.toDoApp.util.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
            throw new PasswordMismatchException("Invalid password");
        }
        return UserMapper.toAuthResponse(user);
    }

    @Override
    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Email not found");
        }
        String token = UUID.randomUUID().toString();
        resetTokens.put(token, request.getEmail());
        return token;
    }

    @Override
    public String resetPassword(ResetPasswordRequest request) {
        UserValidator.validatePasswordMatch(request.getNewPassword().trim(), request.getConfirmNewPassword().trim());
        String email = resetTokens.get(request.getToken());
        if (email == null) {
            throw new TokenNotFoundException("Invalid or expired reset token");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        UserMapper.updatePassword(user, passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        resetTokens.remove(request.getToken());
        return "Password reset successful";
    }
}
