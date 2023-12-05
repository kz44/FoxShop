package com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.MessagePageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Message;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class MessageMapper {
    public MessagePageableDTO toDTO(Message messageEntity) {
        return MessagePageableDTO.builder()
                .content(messageEntity.getContent())
                .sent(messageEntity.getSent())
                .sender(messageEntity.getSender().getId())
                .receiver(messageEntity.getReceiver().getId())
                .isAlreadyRead(messageEntity.isAlreadyRead())
                .build();
    }
}
