package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/conversations")
@AllArgsConstructor
public class ConversationController {

    private MessageService messageService;

    @GetMapping
    protected ResponseEntity<?> getAllConversationsOfLoggedInUser() {
        return ResponseEntity.ok().body(messageService.getConversationInfo());
    }

    @GetMapping("/user1={user1}&user2={user2}/{pageNumber}")
    public ResponseEntity<?> getConversationsBetweenUsers(@PathVariable String user1,
                                                          @PathVariable String user2,
                                                          @PathVariable int pageNumber) {
        return ResponseEntity.ok().body(messageService.getConversationBetweenTwoUsers(user1, user2, pageNumber));
    }
}
