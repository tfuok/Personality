package com.example.Personality.Repositories;

import com.example.Personality.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Book;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(long id);
    List<Booking> findAllByIsDeletedFalse();
    List<Booking> findByUserIdAndIsDeletedFalse(long id);
}
