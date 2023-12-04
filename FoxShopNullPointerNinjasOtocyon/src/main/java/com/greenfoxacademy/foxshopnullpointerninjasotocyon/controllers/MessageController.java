package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam String receiverUsername,
                                         @RequestParam String content) {

        if ((receiverUsername == null || receiverUsername.isEmpty()) && (content == null || content.isEmpty())) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username and content"));
        } else if (receiverUsername == null || receiverUsername.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing username"));
        } else if (content == null || content.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Missing content"));
        }
        return messageService.sendMessage(receiverUsername, content);
    }
}
