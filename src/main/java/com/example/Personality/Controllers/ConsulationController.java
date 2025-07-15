package com.example.Personality.Controllers;

import com.example.Personality.Models.Consulation;
import com.example.Personality.Models.Question;
import com.example.Personality.Requests.BookingRequest;
import com.example.Personality.Requests.ConsulationRequest;
import com.example.Personality.Requests.QuestionRequest;
import com.example.Personality.Services.ConsultantService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consulation")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class ConsulationController {
    @Autowired
    private ConsultantService consultantService;

    @PostMapping("/create-meeting")
    public ResponseEntity<Consulation> createMeeting(@RequestBody ConsulationRequest request) {
        Consulation meeting = consultantService.createMeeting(request);
        return ResponseEntity.ok(meeting);
    }

}
