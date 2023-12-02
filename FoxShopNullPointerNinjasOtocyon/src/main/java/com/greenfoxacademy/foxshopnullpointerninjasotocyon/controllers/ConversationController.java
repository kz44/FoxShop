package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/conversations")
@AllArgsConstructor
public class ConversationController {

    private UserService userService;
    private MessageService messageService;

    @GetMapping
    protected ResponseEntity<?> getAllConversationsOfLoggedInUser() {
        User user = userService.getUserFromSecurityContextHolder();
        return ResponseEntity.ok(messageService.getConversationInfo(user));
    }
}
