package com.example.Personality.Requests;

import lombok.Data;

import java.util.Date;

@Data
public class BookingRequest {
    Date bookTime;
    String description;
}
