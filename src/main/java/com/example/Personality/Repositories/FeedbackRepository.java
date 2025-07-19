package com.example.Personality.Repositories;

import com.example.Personality.Models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findAllByIsDeletedFalse();
    List<Feedback> findByUserIdAndIsDeletedFalse(long id);
}
