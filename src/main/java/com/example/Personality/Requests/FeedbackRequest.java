package com.example.Personality.Requests;

import com.example.Personality.Models.Test;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class FeedbackRequest {

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @Size(max = 200, message = "Feedback must not exceed 200 words")
    private String feedbackContent;

    @NotBlank(message = "Must have Test ID")
    private long TestId;
}
