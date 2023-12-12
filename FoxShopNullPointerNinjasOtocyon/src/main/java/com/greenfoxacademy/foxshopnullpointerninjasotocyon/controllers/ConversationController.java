package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/conversations")
@AllArgsConstructor
public class ConversationController {

    private MessageService messageService;

    @GetMapping
    protected ResponseEntity<?> getAllConversationsOfLoggedInUser() {
        return messageService.getConversationInfo();
    }

    @GetMapping(value = {"/{pageNumber}", "/{pageNumber}/"})
    public ResponseEntity<?> getConversationsBetweenUsers(@RequestParam(required = false) String user1,
                                                          @RequestParam(required = false) String user2,
                                                          @PathVariable(required = false) Integer pageNumber) {
        if (pageNumber == null) {
            pageNumber = 0;
        }
        return messageService.getConversationBetweenTwoUsers(user1, user2, pageNumber);
    }
}
