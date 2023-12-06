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
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    /**
     * Sends a message to the specified user and persists the message in the repository.
     *
     * @param receiverUsername The username of the message recipient.
     * @param content          The content of the message.
     * @return ResponseEntity containing information about the status of the message sending:
     * - 200 OK and a success message in the response body for a successful request.
     * - 400 Bad Request and an error message in the response body if the recipient is not registered.
     */
    @Override
    public ResponseEntity<?> sendMessageByUsername(String receiverUsername, String content) {
        final var receiver = userService.getUserByUsername(receiverUsername);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The given receiver username is not registered"));
        }

        messageRepository.save(Message.builder()
                .content(content)
                .sender(userService.getUserFromSecurityContextHolder())
                .receiver(receiver)
                .sent(LocalDateTime.now())
                .build());

        return ResponseEntity.ok().body(new SuccessMessageDTO("Message successfully send to " + receiver.getUsername()));
    }

    @Override
    public ResponseEntity<?> editMessage(String newContent) {
        final var sender = userService.getUserFromSecurityContextHolder();
        var message = messageRepository.findMessageByUserIdAndSeenFalseAndWithin10MinutesDescLimit1(sender.getId());

        if (message.isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is no message to edit within 10 minutes"));
        }

        message.get().setContent(newContent);
        message.get().setSent(LocalDateTime.now());

        messageRepository.save(message.get());
        return ResponseEntity.ok().body(new SuccessMessageDTO("Message was successfully edited"));
    }
}
