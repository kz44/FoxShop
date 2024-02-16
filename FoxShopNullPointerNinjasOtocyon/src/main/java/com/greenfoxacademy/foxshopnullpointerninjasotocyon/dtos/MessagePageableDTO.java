package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class MessagePageableDTO {
    private String content;
    private LocalDateTime sent;
    private String senderUsername;
    private String receiverUsername;
    private boolean seen;
}
