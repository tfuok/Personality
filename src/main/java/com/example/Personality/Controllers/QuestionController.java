package com.example.Personality.Controllers;

import com.example.Personality.Models.Question;
import com.example.Personality.Models.Test;
import com.example.Personality.Requests.QuestionRequest;
import com.example.Personality.Requests.TestRequest;
import com.example.Personality.Responses.QuestionResponse;
import com.example.Personality.Services.QuestionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class QuestionController {
    @Autowired
    QuestionService questionService;

    @GetMapping
    public ResponseEntity getAllQuestions() {
        List<Question> questions = questionService.getAll();
        return ResponseEntity.ok(questions);
    }
    @GetMapping("/{id}")
    public ResponseEntity getQuestionByTestId(@PathVariable long id) {
        List<QuestionResponse> response = questionService.getByTestId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity createQuestion(@RequestBody QuestionRequest questionRequest) {
        Question question = questionService.create(questionRequest);
        return ResponseEntity.ok(question);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTest(@PathVariable long id) {
        Question question = questionService.delete(id);
        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody QuestionRequest questionRequest) {
        Question question = questionService.updateQuestion(questionRequest, id);
        return ResponseEntity.ok(question);
    }
}
