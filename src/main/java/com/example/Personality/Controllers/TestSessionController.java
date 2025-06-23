package com.example.Personality.Controllers;

import com.example.Personality.Requests.CompleteTestSessionRequest;
import com.example.Personality.Requests.TestSessionRequest;
import com.example.Personality.Responses.AnswerReviewResponse;
import com.example.Personality.Responses.TestSessionResponse;
import com.example.Personality.Services.TestSessionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-sessions")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class TestSessionController {

    @Autowired
    private TestSessionService testSessionService;

    /**
     * API tạo session làm bài thi
     */
    @PostMapping("/create")
    public ResponseEntity<TestSessionResponse> createSession(@RequestBody TestSessionRequest request) {
        TestSessionResponse response = testSessionService.createTestSession(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{sessionId}/complete")
    public ResponseEntity<Void> completeSession(
            @PathVariable Long sessionId,
            @RequestBody CompleteTestSessionRequest request
    ) {
        testSessionService.completeTestSession(sessionId, request.getAnswers());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sessionId}/review")
    public ResponseEntity<List<AnswerReviewResponse>> reviewAnswers(@PathVariable Long sessionId) {
        List<AnswerReviewResponse> reviewList = testSessionService.getReviewOfSession(sessionId);
        return ResponseEntity.ok(reviewList);
    }

}
