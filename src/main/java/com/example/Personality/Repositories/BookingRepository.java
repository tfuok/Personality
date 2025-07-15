package com.example.Personality.Repositories;

import com.example.Personality.Models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findById(long id);
}
