package com.example.Personality.Responses;

import lombok.Data;

import java.util.Date;

@Data
public class TestSessionResponse {
     Long sessionId;
     Long testId;
     Date startTime;
}
