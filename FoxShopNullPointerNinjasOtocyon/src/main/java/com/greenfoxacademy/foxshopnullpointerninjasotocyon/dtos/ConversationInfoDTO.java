package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationInfoDTO {
    private LocalDateTime lastMessageTime;
    private boolean isLastMessageAlreadyRead;
    private long numberOfMessages;
}
