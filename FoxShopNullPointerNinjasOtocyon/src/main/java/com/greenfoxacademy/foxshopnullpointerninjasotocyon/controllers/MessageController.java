package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    @GetMapping(value = {"/{otherUsername}/{pageNumber}", "/{otherUsername}", "/{otherUsername}/"})
    protected ResponseEntity<?> showMessagesWithOtherUser(@PathVariable(required = false) String otherUsername,
                                                          @PathVariable(required = false) Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (otherUsername == null || otherUsername.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Please provide a valid username."));
        }
        return messageService.getMessagesPagination(otherUsername, pageNumber);
    }
}
