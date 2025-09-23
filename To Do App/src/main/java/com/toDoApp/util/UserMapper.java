package com.toDoApp.util;

import com.toDoApp.data.model.User;
import com.toDoApp.dto.request.RegisterRequest;
import com.toDoApp.dto.response.AuthResponse;

public class UserMapper {

    public static User toUser(RegisterRequest request, String encodedPassword) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        return user;
    }

    public static void updatePassword(User user, String encodedPassword) {
        user.setPassword(encodedPassword);
    }

    public static AuthResponse toAuthResponse(User user) {
        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
