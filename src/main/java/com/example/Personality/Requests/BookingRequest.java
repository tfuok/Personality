package com.example.Personality.Requests;

import lombok.Data;

import java.util.Date;

@Data
public class BookingRequest {
    String userId;
    Date bookTime;
    String description;
}
