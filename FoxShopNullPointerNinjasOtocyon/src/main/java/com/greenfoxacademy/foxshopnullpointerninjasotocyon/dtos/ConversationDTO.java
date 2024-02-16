package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConversationDTO {
    private String username;
    private ConversationInfoDTO details;
}
