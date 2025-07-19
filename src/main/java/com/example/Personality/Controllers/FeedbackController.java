package com.example.Personality.Controllers;

import com.example.Personality.Models.Feedback;
import com.example.Personality.Requests.FeedbackRequest;
import com.example.Personality.Services.FeedbackService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class FeedbackController {
    @Autowired
    FeedbackService feedbackService;

    @PostMapping()
    public ResponseEntity feedback (@Valid @RequestBody FeedbackRequest feedbackRequest){
        feedbackService.Feedback(feedbackRequest);
        return ResponseEntity.ok("Feedback successfully");
    }

    @GetMapping("/getAll")
    public ResponseEntity getAllFeedback (){
        List<Feedback> feedbacks = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity getFeedbackById (@PathVariable long id){
        List<Feedback> feedbacks = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedbacks);
    }
}
