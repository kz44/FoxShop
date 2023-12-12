package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
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
    public ResponseEntity<?> sendMessageByUsername(String receiverUsername, MessageDTO content) {
        final var receiver = userService.getUserByUsername(receiverUsername);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The given receiver username is not registered"));
        }

        messageRepository.save(Message.builder()
                .content(content.getContent())
                .sender(userService.getUserFromSecurityContextHolder())
                .receiver(receiver)
                .sent(LocalDateTime.now())
                .build());

        return ResponseEntity.ok().body(new SuccessMessageDTO("Message successfully send to " + receiver.getUsername()));
    }

    /**
     * Edits the content of the latest unseen message sent by the authenticated user within the last 10 minutes.
     *
     * @param receiverUsername The receiver username
     * @param newContent       The new content to set for the message.
     * @return ResponseEntity, the status of the edit operation:
     * - 200 OK and a success message in the response body for a successful edit.
     * - 400 Bad Request and an error message in the response body if there is no eligible message within 10 minutes.
     */
    @Override
    public ResponseEntity<?> editMessage(String receiverUsername, MessageDTO newContent) {
        final var sender = userService.getUserFromSecurityContextHolder();
        final var receiver = userService.getUserByUsername(receiverUsername);
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(10);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There isn't user with the given username"));
        }

        var messageOpt = messageRepository.findUnseenMessageWithinMinutesDescLimit1(sender.getId(), receiver.getId(), timeThreshold);

        if (messageOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is no message to edit within 10 minutes"));
        }

        var message = messageOpt.get();
        message.setContent(newContent.getContent());
        message.setSent(LocalDateTime.now());

        messageRepository.save(message);
        return ResponseEntity.ok().body(new SuccessMessageDTO("Message was successfully edited"));
    }


    /**
     * Deletes the last unseen message sent by the authenticated user to the specified receiver
     * if the message was sent within the last 10 minutes.
     *
     * @param receiverUsername The username of the message receiver.
     * @return ResponseEntity containing either a success message if the message
     * was successfully deleted or an error message
     * if there was no message to delete or
     * if the specified receiver does not exist.
     */
    @Override
    public ResponseEntity<?> deleteLastMessage(String receiverUsername) {
        final var sender = userService.getUserFromSecurityContextHolder();
        final var receiver = userService.getUserByUsername(receiverUsername);
        LocalDateTime timeThreshold = LocalDateTime.now().minusMinutes(10);

        if (receiver == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There isn't user with the given username"));
        }

        var messageOpt = messageRepository.findUnseenMessageWithinMinutesDescLimit1(sender.getId(), receiver.getId(), timeThreshold);

        if (messageOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There are either no new messages within last 10 minutes or they have already been read."));
        }

        var message = messageOpt.get();
        messageRepository.delete(message);
        return ResponseEntity.ok().body(new SuccessMessageDTO("Message was successfully deleted"));
    }
}
