package com.example.Personality.Services;

import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Feedback;
import com.example.Personality.Models.Test;
import com.example.Personality.Repositories.FeedbackRepository;
import com.example.Personality.Repositories.TestRepository;
import com.example.Personality.Repositories.UserRepository;
import com.example.Personality.Requests.FeedbackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TestRepository testRepository;
    public void Feedback(FeedbackRequest feedbackRequest){
        Feedback feedback = new Feedback();
        Test test = testRepository.findTestByIdAndIsDeletedFalse(feedbackRequest.getTestId());
        feedback.setFeedbackContent(feedbackRequest.getFeedbackContent());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setUser(userService.getCurrentAccount());
        feedback.setTest(test);
        feedbackRepository.save(feedback);
    }

    public List<Feedback> getAllFeedbacks(){
        return feedbackRepository.findAllByIsDeletedFalse();
    }

    public List<Feedback> getFeedbackById(long id){
        List<Feedback> feedbacks = feedbackRepository.findByUserIdAndIsDeletedFalse(id);
        if(feedbacks == null) throw new NotFound("Feedback not found");
        return feedbacks;
    }
}
