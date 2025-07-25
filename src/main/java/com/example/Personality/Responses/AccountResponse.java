package com.example.Personality.Responses;

import com.example.Personality.Models.Role;
import lombok.Data;

@Data
public class AccountResponse {
    long id;
    String email;
    String phone;
    String fullName;
    Role role;
    String token;
}
