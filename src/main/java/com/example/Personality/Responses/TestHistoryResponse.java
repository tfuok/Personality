package com.example.Personality.Responses;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TestHistoryResponse {
    Long sessionId;
    List<AnswerReviewResponse> answerReviewResponses;
    Date startTime;
    Date endTime;
}
