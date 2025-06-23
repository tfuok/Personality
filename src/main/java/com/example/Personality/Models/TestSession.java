package com.example.Personality.Models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TestSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date startTime;

    private Date endTime;

    private String result;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    // JSON string lưu map QuestionId -> Đánh giá 1-5
    @Column(columnDefinition = "TEXT")
    private String answerJson;

    @Transient
    private Map<Long, Integer> answers = new HashMap<>();

    @PostLoad
    public void parseAnswers() {
        try {
            if (answerJson != null && !answerJson.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                answers = mapper.readValue(answerJson, new TypeReference<Map<Long, Integer>>() {});
            }
        } catch (Exception e) {
            answers = new HashMap<>();
        }
    }

    @PrePersist
    @PreUpdate
    public void serializeAnswers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            answerJson = mapper.writeValueAsString(answers);
        } catch (Exception e) {
            answerJson = "{}";
        }
    }
}
