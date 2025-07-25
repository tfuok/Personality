package com.example.Personality.Services;

import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Booking;
import com.example.Personality.Models.User;
import com.example.Personality.Repositories.BookingRepository;
import com.example.Personality.Repositories.UserRepository;
import com.example.Personality.Requests.BookingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    public void booking(BookingRequest bookingRequest){
        Booking booking = new Booking();
        booking.setBookTime(bookingRequest.getBookTime());
        booking.setUser(userService.getCurrentAccount());
        booking.setCreateAt(new Date());
        booking.setDescription(bookingRequest.getDescription());
        bookingRepository.save(booking);
    }

    public List<Booking> getAllById(long id){
        User user = userRepository.findUserByIdAndIsDeletedFalse(id);
        if(user == null) throw new NotFound("User not exist");
        List<Booking> bookings = bookingRepository.findByUserIdAndIsDeletedFalse(id);

        return bookings;
    }


    public void deleteBooking(long id){
        Booking booking = bookingRepository.findById(id);
        if(booking == null) throw new NotFound("Not found");

        booking.setDeleted(true);
        bookingRepository.save(booking);
    }

    public List<Booking> getAll(){
        return bookingRepository.findAllByIsDeletedFalse();
    }
}
