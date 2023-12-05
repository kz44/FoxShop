package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessagePageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    @GetMapping("/{otherUsername}/{pageNumber}")
    protected ResponseEntity<?> showMessagesWithOtherUser(@PathVariable(required = false) String otherUsername,
                                                       @PathVariable(required = false) int pageNumber,
                                                       Pageable pageable) {
        ResponseEntity<?> messagePage = messageService.getMessagesPagination(otherUsername, pageNumber, pageable);
        return ResponseEntity.ok().body(messagePage);
    }
}
