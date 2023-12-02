package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/conversations")
@AllArgsConstructor
public class ConversationController {

    private MessageService messageService;

    @GetMapping
    protected ResponseEntity<?> getAllConversationsOfLoggedInUser() {
        return ResponseEntity.ok(messageService.getConversationInfo());
    }
}
