package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
  
    @GetMapping(value = {"/{otherUsername}/{pageNumber}", "/{otherUsername}", "/{otherUsername}/"})
    protected ResponseEntity<?> showMessagesWithOtherUser(@PathVariable String otherUsername,
                                                          @PathVariable(required = false) Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        return messageService.getMessagesPagination(otherUsername, pageNumber);

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

        if ((receiverUsername == null) && (content == null || content.getContent().isEmpty())) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username and content"));
        }

        if (receiverUsername == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username"));
        }

        if (content == null || content.getContent().isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing content"));
        }

        return messageService.sendMessageByUsername(receiverUsername, content);
    }
}
