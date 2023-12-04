package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final UserService userService;

    @Override
    public ResponseEntity<?> sendMessage(String receiverUsername, String content) {
        final var receiver = userService.getUserByUsername(receiverUsername);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The given receiver username is not registered"));
        } else {
            messageRepository.save(Message.builder()
                    .content(content)
                    .sender(userService.getUserFromSecurityContextHolder())
                    .receiver(receiver)
                    .sent(LocalDateTime.now())
                    .build());

            return ResponseEntity.ok().body(new SuccessMessageDTO("Message successfully send to " + receiver.getUsername()));
        }
    }
}
