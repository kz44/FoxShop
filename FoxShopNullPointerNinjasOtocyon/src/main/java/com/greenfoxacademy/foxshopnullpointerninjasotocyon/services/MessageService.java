package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import org.springframework.http.ResponseEntity;

public interface MessageService {
    ResponseEntity<?> getConversationInfo(User user);
}
