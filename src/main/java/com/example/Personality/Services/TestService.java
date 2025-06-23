package com.example.Personality.Services;

import com.example.Personality.Exception.DuplicatedEntity;
import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Test;
import com.example.Personality.Repositories.TestRepository;
import com.example.Personality.Requests.TestRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    TestRepository testRepository;
    @Autowired
    UserService userService;
    public List<Test> getAllTests() {
        List<Test> tests = testRepository.findTestByIsDeletedFalse();
        return tests;
    }

    public Test createNewTest(TestRequest testRequest) {
        try {
            Test findTest = testRepository.findTestByTitleAndIsDeletedFalse(testRequest.getTitle());
            if (findTest != null) {
                throw new DuplicatedEntity("Test existed!");
            }

            Test newTest = new Test();
            newTest.setTitle(testRequest.getTitle());
            newTest.setDescription(testRequest.getDescription());
            newTest.setUser(userService.getCurrentAccount());
            return testRepository.save(newTest);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Test deleteTest(long id) {
        Test test = testRepository.findTestByIdAndIsDeletedFalse(id);
        if (test == null) throw new NotFound("Test not exist");
        test.setDeleted(true);
        return testRepository.save(test);
    }

    public Test updateTest(long id, TestRequest testRequest){
        Test test = testRepository.findTestByIdAndIsDeletedFalse(id);
        if (test == null) throw new NotFound("Test not exist");
        test.setTitle(testRequest.getTitle());
        test.setDescription(testRequest.getDescription());
        return testRepository.save(test);
    }
}
