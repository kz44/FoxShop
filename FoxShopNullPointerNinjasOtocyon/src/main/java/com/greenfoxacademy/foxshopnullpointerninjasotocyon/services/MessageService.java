package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import org.springframework.http.ResponseEntity;

public interface MessageService {
    ResponseEntity<?> getConversationInfo();

    ResponseEntity<?> getMessagesPagination(String otherUsername, int pageNumber);

    ResponseEntity<?> getConversationBetweenTwoUsers(String user1, String user2, int pageNumber);
}
