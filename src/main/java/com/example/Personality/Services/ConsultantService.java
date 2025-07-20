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
import java.util.List;
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

        Set<User> validUsers = new HashSet<>();
        for (String email : request.getConsultMembersEmail()) {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new NotFound("Không tìm thấy người dùng với email: " + email);
            }
            validUsers.add(user);
        }

        meeting.setUsers(validUsers);
        meeting.setDeleted(false);
        meeting.setScheduledTime(request.getScheduledTime());
        return meetingRepository.save(meeting);
    }

    public List<Consulation> getAll() {
        return meetingRepository.findAllByIsDeletedFalse();
    }

    public void delete(long id) {
        Consulation meeting = meetingRepository.findByIdAndIsDeletedFalse(id);
        if (meeting == null) {
            throw new NotFound("Không tìm thấy lịch tư vấn để xoá");
        }
        meeting.setDeleted(true);
        meetingRepository.save(meeting);
    }

    public List<Consulation> getByUserId(long userId) {
        List<Consulation> meetings = meetingRepository.findByUsers_IdAndIsDeletedFalse(userId);
        if (meetings.isEmpty()) {
            throw new NotFound("Không tìm thấy lịch tư vấn cho user này");
        }
        return meetings;
    }
}
