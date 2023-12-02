package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    @GetMapping("/{otherUsername}/{pageNumber}")
    protected ResponseEntity<?> showMessagesWithOtherUser(@PathVariable String otherUsername,
                                                     @PathVariable int pageNumber,
                                                     Pageable pageable) {
        Page<Message> messagesPage = messageService.getMessagesWithOtherUser(otherUsername, pageNumber, pageable);
        if (messagesPage != null) {
            return ResponseEntity.ok().body(messagesPage.getContent());
        }
        return ResponseEntity.ok(new SuccessMessageDTO("There are no messages between these users yet."));
    }
}
