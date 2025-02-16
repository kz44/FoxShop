package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SuccessMessageDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
    private String success;

    public SuccessMessageDTO(String success) {
        this.success = success;
    }
}
