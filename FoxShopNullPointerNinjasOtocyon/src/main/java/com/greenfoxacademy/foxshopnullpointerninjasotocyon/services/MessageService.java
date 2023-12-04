package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<?> sendMessage(String receiverUsername, String content);
}
