package com.example.Personality.Requests;

import java.util.Map;

public class CompleteTestSessionRequest {

    private Map<Long, Integer> answers;

    public Map<Long, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, Integer> answers) {
        this.answers = answers;
    }
}
