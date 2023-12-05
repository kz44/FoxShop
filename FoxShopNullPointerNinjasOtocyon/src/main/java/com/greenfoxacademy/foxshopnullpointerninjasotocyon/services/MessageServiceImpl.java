package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper.MessageMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private UserService userService;
    private MessageMapper messageMapper;

    @Override
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

    @Override
    public ResponseEntity<?> getMessagesPagination(String otherUsername,
                                                   int pageNumber,
                                                   Pageable pageable) {
        User user = userService.getUserFromSecurityContextHolder();
        Optional<User> otherUser = userRepository.findByUsername(otherUsername);
        if (!otherUser.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("User does not exist."));
        }
        // Sort sort = Sort.by(Sort.Direction.DESC, "sent");
        int pageSize = 10;
        Page<MessagePageableDTO> messagePagedAndSorted = messageRepository.findMessagesBetweenUsers(
                user,
                otherUser.get(),
                PageRequest.of(pageNumber, pageSize)).map(messageMapper::toDTO);
        return ResponseEntity.ok().body(messagePagedAndSorted);
    }
}
