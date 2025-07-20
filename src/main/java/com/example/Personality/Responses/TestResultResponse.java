package com.example.Personality.Responses;

import com.example.Personality.Models.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultResponse {
    private String result;
    private long userId;
    private Test test;
}
