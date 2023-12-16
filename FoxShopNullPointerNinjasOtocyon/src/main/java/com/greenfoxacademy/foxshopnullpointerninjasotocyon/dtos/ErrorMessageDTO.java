package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ErrorMessageDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    private String error;

    public ErrorMessageDTO(String error) {
        this.error = error;
    }
}
