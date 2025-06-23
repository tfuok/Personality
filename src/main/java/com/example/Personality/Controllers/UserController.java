package com.example.Personality.Controllers;

import com.example.Personality.Models.User;
import com.example.Personality.Requests.LoginRequest;
import com.example.Personality.Requests.RegisterRequest;
import com.example.Personality.Responses.AccountResponse;
import com.example.Personality.Services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity getAllAccount() {
        List<User> accounts = userService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterRequest registerRequest){
        AccountResponse user = userService.register(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        AccountResponse newAccount = userService.login(loginRequest);
        return ResponseEntity.ok(newAccount);
    }
}
