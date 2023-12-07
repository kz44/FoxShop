package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MessagePageableDTO {
    private String content;
    private LocalDateTime sent;
    private Long sender;
    private Long receiver;
    private boolean isAlreadyRead;
}
