package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessageDTO;
import org.springframework.http.ResponseEntity;

public interface MessageService {
  
    ResponseEntity<?> getConversationInfo();

    ResponseEntity<?> getMessagesPagination(String otherUsername, Integer pageNumber);

    ResponseEntity<?> sendMessageByUsername(String receiverUsername, MessageDTO content);
}
