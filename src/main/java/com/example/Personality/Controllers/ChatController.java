package com.example.Personality.Controllers;

import com.example.Personality.Models.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage send(ChatMessage message) throws Exception {
        System.out.println("Received message:" + message);
        return message;
    }

    @MessageMapping("/leave")
    @SendTo("/topic/messages")
    public ChatMessage leave(String username) throws Exception{
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(username + "left the chat");
        chatMessage.setTimestamp(java.time.LocalTime.now().toString());
        chatMessage.setSender("System");
        return chatMessage;
    }

    @MessageMapping("/join")
    @SendTo("/topic/messages")
    public ChatMessage join(String username) throws Exception{
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(username + "join the chat");
        chatMessage.setTimestamp(java.time.LocalTime.now().toString());
        chatMessage.setSender("System");
        return chatMessage;
    }
}
