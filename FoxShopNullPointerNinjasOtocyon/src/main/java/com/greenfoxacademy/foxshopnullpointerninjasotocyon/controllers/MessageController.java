package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    /**
     * Endpoint for sending a message to a specified user.
     *
     * @param receiverUsername The username of the recipient.
     * @param content          The content of the message.
     * @return ResponseEntity containing information about the status of the message sending:
     * - 200 OK and details of the sent message in the response body for a successful request with the given username.
     * - 400 Bad Request and an error message in the response body for a failed request.
     */
    @PostMapping(value = {"/send/{receiverUsername}", "/send/", "/send"})
    public ResponseEntity<?> sendMessage(@PathVariable(required = false) String receiverUsername,
                                         @RequestBody(required = false) MessageDTO content) {

        if ((receiverUsername == null || receiverUsername.isEmpty()) && (content == null || content.getContent().isEmpty())) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username and content"));
        }

        if (receiverUsername == null || receiverUsername.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username"));
        }

        if (content == null || content.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing content"));
        }

        return messageService.sendMessageByUsername(receiverUsername, content);
    }

    /**
     * Endpoint for editing a message
     *
     * @param receiverUsername The username of the recipient.
     * @param newContent the new content.
     * @return ResponseEntity containing information about the status of the message sending:
     * - 200 OK and successful message in the response body for a successful request.
     * - 400 Bad Request and an error message in the response body for a failed request.
     */
    @PutMapping(value = {"/edit/{receiverUsername}", "/edit/", "/edit"})
    public ResponseEntity<?> editMessage(@PathVariable (required = false) String receiverUsername,
                                         @RequestBody(required = false) MessageDTO newContent) {

        if (receiverUsername == null && (newContent == null || newContent.getContent().isEmpty())) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username and content"));
        }

        if (receiverUsername == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username"));
        }

        if (newContent == null || newContent.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing content for editing the message"));
        }

        return messageService.editMessage(receiverUsername, newContent);
    }
}
