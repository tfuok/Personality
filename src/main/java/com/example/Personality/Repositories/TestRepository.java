package com.example.Personality.Repositories;

import com.example.Personality.Models.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findTestByIsDeletedFalse();

    Test findTestByTitleAndIsDeletedFalse(String title);

    Test findTestByIdAndIsDeletedFalse(Long id);
}
