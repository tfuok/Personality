package com.example.Personality.Services;

import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Test;
import com.example.Personality.Models.TestSession;
import com.example.Personality.Models.User;
import com.example.Personality.Repositories.QuestionRepository;
import com.example.Personality.Repositories.TestRepository;
import com.example.Personality.Repositories.TestSessionRepository;
import com.example.Personality.Repositories.UserRepository;
import com.example.Personality.Requests.TestSessionRequest;
import com.example.Personality.Responses.AnswerReviewResponse;
import com.example.Personality.Responses.TestSessionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TestSessionService {
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestSessionRepository testSessionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionRepository questionRepository;
    public TestSessionResponse createTestSession(TestSessionRequest request) {
        Test test = testRepository.findById(request.getTestId())
                .orElseThrow(() -> new NotFound("Test not found"));

        TestSession session = new TestSession();
        session.setTest(test);
        session.setUser(userService.getCurrentAccount());
        session.setStartTime(new Date());

        TestSession saved = testSessionRepository.save(session);

        TestSessionResponse response = new TestSessionResponse();
        response.setSessionId(saved.getId());
        response.setTestId(saved.getTest().getId());
        response.setStartTime(saved.getStartTime());

        return response;
    }
    public void completeTestSession(Long sessionId, Map<Long, Integer> answers) {
        TestSession session = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFound("Session not found"));

        if (session.getAnswers() == null) {
            session.setAnswers(new HashMap<>());
        }

        // Validate và lưu toàn bộ đáp án gửi từ client
        for (Map.Entry<Long, Integer> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            Integer rating = entry.getValue();

            if (rating < 1 || rating > 5) {
                throw new IllegalArgumentException("Mức độ cho phép là từ 1 đến 5");
            }

//            boolean validQuestion = questionRepository.existsByIdAndTestId(questionId, session.getTest().getId());
//            if (!validQuestion) {
//                throw new NotFound("Câu hỏi " + questionId + " không thuộc bài thi này");
//            }

            session.getAnswers().put(questionId, rating);
        }

        session.setEndTime(new Date());

        testSessionRepository.save(session);
    }

    public List<AnswerReviewResponse> getReviewOfSession(Long sessionId) {
        TestSession session = testSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFound("Session not found"));

        Map<Long, Integer> answers = session.getAnswers();

        if (answers == null || answers.isEmpty()) {
            throw new NotFound("Chưa có đáp án được lưu cho session này");
        }

        List<AnswerReviewResponse> reviewList = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            Integer rating = entry.getValue();

            String content = questionRepository.findById(questionId)
                    .map(q -> q.getContent())
                    .orElse("Nội dung câu hỏi không tồn tại");

            AnswerReviewResponse review = new AnswerReviewResponse(questionId, content, rating);
            reviewList.add(review);
        }

        return reviewList;
    }


//    //    public void completeTestSession(Long sessionId, String result) {
//        public void completeTestSession(Long sessionId) {
//        TestSession session = testSessionRepository.findById(sessionId)
//                .orElseThrow(() -> new NotFound("Session not found"));
//
//        session.setEndTime(new Date());
////        session.setResult(result);
//        testSessionRepository.save(session);
//    }
}
