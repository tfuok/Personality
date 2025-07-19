package com.example.Personality.Controllers;

import com.example.Personality.Models.User;
import com.example.Personality.Requests.LoginRequest;
import com.example.Personality.Requests.RegisterRequest;
import com.example.Personality.Requests.UpdateRequest;
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

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest){
        userService.register(registerRequest);
        return ResponseEntity.ok("Register successfully!");
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest) {
        AccountResponse newAccount = userService.login(loginRequest);
        return ResponseEntity.ok(newAccount);
    }

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity updateAccount(@RequestBody UpdateRequest request, @PathVariable long id) {
        User account = userService.updateAccount(request, id);
        return ResponseEntity.ok("Update Success");
    }
    @PostMapping("resetPassword")
    public ResponseEntity resetPassword(String newPassword) {
        userService.resetPassword(newPassword);
        return ResponseEntity.ok("Reset successfully");
    }

    @PostMapping("/{parentId}/set-student")
    public ResponseEntity<String> linkStudent(
            @PathVariable Long parentId, String request
    ) {
        userService.linkStudentToParent(parentId, request);
        return ResponseEntity.ok("Đã gán học sinh cho phụ huynh thành công.");
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<User>> getChildren(@PathVariable Long parentId) {
        List<User> children = userService.getChildrenOfParent(parentId);
        return ResponseEntity.ok(children);
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id){
        User user = userService.deleteUser(id);
        return ResponseEntity.ok(user);
    }
}
