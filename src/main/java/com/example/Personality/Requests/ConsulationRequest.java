package com.example.Personality.Requests;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ConsulationRequest {
    String googleMeetURL;
    Set<String> consultMembersEmail;
}
