package com.toDoApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
