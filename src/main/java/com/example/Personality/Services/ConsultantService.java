package com.example.Personality.Services;

import com.example.Personality.Exception.NotFound;
import com.example.Personality.Models.Consulation;
import com.example.Personality.Models.User;
import com.example.Personality.Repositories.ConsulationRepository;
import com.example.Personality.Repositories.UserRepository;
import com.example.Personality.Requests.ConsulationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ConsultantService {

    @Autowired
    private ConsulationRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    public Consulation createMeeting(ConsulationRequest request) {
        Consulation meeting = new Consulation();
        meeting.setGoogleMeetLink(request.getGoogleMeetURL());
        meeting.setScheduledTime(request.getScheduleDate());

        Set<User> validUsers = new HashSet<>();
        for (String email : request.getConsultMembersEmail()) {
            User user = userRepository.findByEmail(email);
            if (user == null) throw new NotFound("Không tìm thấy người dùng với email: " + email);
            validUsers.add(user);
        }

        meeting.setUsers(validUsers);

        return meetingRepository.save(meeting);
    }
}
