package com.example.Personality.Repositories;

import com.example.Personality.Models.TestSession;
import com.example.Personality.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestSessionRepository extends JpaRepository<TestSession, Long> {
    List<TestSession> findByUser(User user);
}
