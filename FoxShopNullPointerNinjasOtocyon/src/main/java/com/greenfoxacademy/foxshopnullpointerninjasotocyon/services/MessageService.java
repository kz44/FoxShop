package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import org.springframework.http.ResponseEntity;

public interface MessageService {

    ResponseEntity<?> sendMessageByUsername(String receiverUsername, MessageDTO content);

    ResponseEntity<?> editMessage(String receiverUsername, MessageDTO newContent);

    ResponseEntity<?> deleteLastMessage(String receiverUsername);
}
