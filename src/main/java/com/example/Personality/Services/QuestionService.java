package com.example.Personality.Services;

import com.example.Personality.Exception.DuplicatedEntity;
import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Question;
import com.example.Personality.Models.Test;
import com.example.Personality.Repositories.QuestionRepository;
import com.example.Personality.Repositories.TestRepository;
import com.example.Personality.Requests.QuestionRequest;
import com.example.Personality.Responses.QuestionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TestRepository testRepository;

    public List<Question> getAll(){
        return questionRepository.findAllByIsDeletedFalse();
    }

    public Question delete(long id){
        Question question = questionRepository.findByIdAndIsDeletedFalse(id);
        if(question == null) throw new NotFound("Question Not Found");
        question.setDeleted(true);
        return questionRepository.save(question);
    }

    public Question create(QuestionRequest questionRequest){
        Question findQuestion = questionRepository.findByContentAndIsDeletedFalse(questionRequest.getContent());
        if (findQuestion != null) throw new DuplicatedEntity("Question existed!");

        Test test = testRepository.findTestByIdAndIsDeletedFalse(questionRequest.getTestId());
        if (test == null) throw new NotFound("Test not found");

        Question question = new Question();
        question.setContent(questionRequest.getContent());
        question.setTest(test);
        return questionRepository.save(question);
    }

    public List<QuestionResponse> getByTestId(long testId){
        Test test = testRepository.findTestByIdAndIsDeletedFalse(testId);
        if(test == null) throw new NotFound("Test not found");

        List<QuestionResponse> questions = new ArrayList<>();
        for(Question question : test.getQuestions()){
            QuestionResponse response = new QuestionResponse();
            response.setId(question.getId());
            response.setContent(question.getContent());
            if(!question.isDeleted()){
                questions.add(response);
            }
        }
        return questions;
    }

    public Question updateQuestion(QuestionRequest questionRequest, long id){
        Question question = questionRepository.findByIdAndIsDeletedFalse(id);
        if(question == null) throw new NotFound("Question not found");
        question.setContent(questionRequest.getContent());
        return questionRepository.save(question);
    }
}
