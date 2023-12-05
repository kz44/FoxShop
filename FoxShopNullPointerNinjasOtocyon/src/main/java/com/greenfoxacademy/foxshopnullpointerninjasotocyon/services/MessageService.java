package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface MessageService {
    ResponseEntity<?> getConversationInfo();

    ResponseEntity<?> getMessagesPagination(String otherUsername, int pageNumber);
}
