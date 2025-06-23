package com.example.Personality.Controllers;

import com.example.Personality.Models.Test;
import com.example.Personality.Requests.TestRequest;
import com.example.Personality.Services.TestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TestController {
    @Autowired
    TestService testService;

    @GetMapping
    public ResponseEntity getAllTests() {
        List<Test> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    @PostMapping
    public ResponseEntity createTest(@RequestBody TestRequest testRequest){
        Test test = testService.createNewTest(testRequest);
        return ResponseEntity.ok(test);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTest(@PathVariable long id){
        Test test = testService.deleteTest( id);
        return ResponseEntity.ok(test);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody TestRequest testRequest){
        Test test = testService.updateTest(id, testRequest);
        return ResponseEntity.ok(test);
    }
}
