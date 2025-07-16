package com.example.Personality.Requests;

import com.example.Personality.Models.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    String fullName;
    String email;
    String password;
    String phone;
    Role role;
}
