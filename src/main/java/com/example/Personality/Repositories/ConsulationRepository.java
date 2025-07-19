package com.example.Personality.Repositories;

import com.example.Personality.Models.Consulation;
import com.example.Personality.Models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsulationRepository extends JpaRepository<Consulation, Long> {
    List<Consulation> findAllByIsDeletedFalse();
    Consulation findByIdAndIsDeletedFalse(long id);
    List<Consulation> findByUsers_IdAndIsDeletedFalse(Long userId);

}
