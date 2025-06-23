package com.example.Personality.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerReviewResponse {

    private Long questionId;
    private String questionContent;
    private Integer selectedRating;

}
