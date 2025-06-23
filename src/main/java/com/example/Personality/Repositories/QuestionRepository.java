package com.example.Personality.Repositories;

import com.example.Personality.Models.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByIsDeletedFalse();
    Question findByIdAndIsDeletedFalse(long id);
    Question findByContentAndIsDeletedFalse(String content);
}
