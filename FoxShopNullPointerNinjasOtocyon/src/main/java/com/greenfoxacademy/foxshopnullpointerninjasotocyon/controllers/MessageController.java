package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

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

    @GetMapping("/{otherUsername}/{pageNumber}")
    protected ResponseEntity<?> showMessagesWithOtherUser(@PathVariable String otherUsername,
                                                       @PathVariable int pageNumber) {
        return ResponseEntity.ok().body(messageService.getMessagesPagination(otherUsername, pageNumber));
    }
}
