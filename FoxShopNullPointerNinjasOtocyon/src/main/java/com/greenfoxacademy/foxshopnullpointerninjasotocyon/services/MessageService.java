package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<?> sendMessageByUsername(String receiverUsername, String content);

    ResponseEntity<?> editMessage(String newContent);
}
