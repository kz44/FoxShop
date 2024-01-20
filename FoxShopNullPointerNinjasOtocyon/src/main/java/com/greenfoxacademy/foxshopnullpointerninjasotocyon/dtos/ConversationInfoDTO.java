package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConversationInfoDTO {
    private LocalDateTime lastMessageTime;
    private boolean seen;
    private long numberOfMessages;
}
