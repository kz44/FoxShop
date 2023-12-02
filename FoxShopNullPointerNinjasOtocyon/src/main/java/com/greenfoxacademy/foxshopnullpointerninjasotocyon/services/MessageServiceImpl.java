package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    public ResponseEntity<?> getConversationInfo(User user) {
        Map<String, Map<String, Object>> response = new HashMap<>();

        Set<User> otherUsers = messageRepository.findOtherUsers(user);

        for (User otherUser : otherUsers) {
            Map<String, Object> conversationInfo = new HashMap<>();

            Message lastMessage = messageRepository.getLastMessage(user, otherUser);
            LocalDateTime lastMessageTime = lastMessage.getSent();
            boolean lastMessageIsAlreadyRead = lastMessage.isAlreadyRead();
            int numberOfMessages = messageRepository.countMessagesBetweenUsers(user, otherUser);

            conversationInfo.put("lastMessageTime", lastMessageTime);
            conversationInfo.put("lastMessageIsAlreadyRead", lastMessageIsAlreadyRead);
            conversationInfo.put("numberOfMessages", numberOfMessages);

            response.put(otherUser.getUsername(), conversationInfo);
        }
        return ResponseEntity.ok(response);
    }
}
