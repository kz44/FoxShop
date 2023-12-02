package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ConversationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ConversationInfoDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserService userService;

    public ResponseEntity<?> getConversationInfo() {
        User user = userService.getUserFromSecurityContextHolder();
        Set<User> otherUsers = messageRepository.findOtherUsers(user);

        if (otherUsers == null || otherUsers.isEmpty()) {
            return ResponseEntity.ok(new SuccessMessageDTO("You have no conversations with other users yet."));
        }
        List<ConversationDTO> conversations = new ArrayList<>();
        for (User otherUser : otherUsers) {
            Message lastMessage = messageRepository.getLastMessage(user, otherUser);
            LocalDateTime lastMessageTime = lastMessage.getSent();
            boolean isLastMessageAlreadyRead = lastMessage.isAlreadyRead();
            long numberOfMessages = messageRepository.countMessagesBetweenUsers(user, otherUser);

            ConversationDTO conversationDTO = new ConversationDTO(otherUser.getUsername(),
                    new ConversationInfoDTO(lastMessageTime, isLastMessageAlreadyRead, numberOfMessages));
            conversations.add(conversationDTO);
        }
        return ResponseEntity.ok().body(conversations);
    }
}
