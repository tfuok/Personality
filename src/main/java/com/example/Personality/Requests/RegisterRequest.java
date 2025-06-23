package com.example.Personality.Requests;

import com.example.Personality.Models.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    String fullname;
    String email;
    String password;
    String phone;
    Role role;
}
