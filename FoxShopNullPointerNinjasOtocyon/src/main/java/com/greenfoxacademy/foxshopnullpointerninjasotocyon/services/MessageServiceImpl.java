package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
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

    /**
     * Sends a message to the specified user and persists the message in the repository.
     *
     * @param receiverUsername The username of the message recipient.
     * @param content The content of the message.
     * @return ResponseEntity containing information about the status of the message sending:
     *   - 200 OK and a success message in the response body for a successful request.
     *   - 400 Bad Request and an error message in the response body if the recipient is not registered.
     */
    @Override
    public ResponseEntity<?> sendMessage(String receiverUsername, String content) {
        final var receiver = userService.getUserByUsername(receiverUsername);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The given receiver username is not registered"));
        } else {
            Message message = buildMessage(receiverUsername, content, receiver);
            messageRepository.save(message);

            return ResponseEntity.ok().body(new SuccessMessageDTO("Message successfully send to " + receiver.getUsername()));
        }
    }

    /**
     * Builds a message object with the provided parameters.
     *
     * @param receiverUsername The username of the message recipient.
     * @param content The content of the message.
     * @param sender The User object representing the sender of the message.
     * @return A Message object built with the specified parameters.
     */
    public Message buildMessage (String receiverUsername, String content, User sender) {
        return Message.builder()
                .content(content)
                .sender(userService.getUserFromSecurityContextHolder())
                .receiver(sender)
                .sent(LocalDateTime.now())
                .build();
    }
}
