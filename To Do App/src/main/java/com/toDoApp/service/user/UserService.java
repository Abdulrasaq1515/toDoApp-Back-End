package com.toDoApp.service.user;

import com.toDoApp.dto.request.*;
import com.toDoApp.dto.response.AuthResponse;

public interface UserService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    String forgotPassword(ForgotPasswordRequest request);
    String resetPassword(ResetPasswordRequest request);
}
