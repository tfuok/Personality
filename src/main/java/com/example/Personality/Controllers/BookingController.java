package com.example.Personality.Controllers;

import com.example.Personality.Models.Booking;
import com.example.Personality.Requests.BookingRequest;
import com.example.Personality.Services.BookingService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class BookingController {
    @Autowired
    BookingService bookingService;

    @PostMapping
    public ResponseEntity bookingMeeting(@RequestBody BookingRequest request) {
        bookingService.booking(request);
        return ResponseEntity.ok("Book successfully");
    }

    @GetMapping("getByUserId/{id}")
    public ResponseEntity getByUserId(long id) {
        List<Booking> bookings = bookingService.getAllById(id);
        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(long id) {
         bookingService.deleteBooking(id);
        return ResponseEntity.ok("Deleted");
    }
}
