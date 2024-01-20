package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ImageOperationSuccessDTO {

    private String completed;

    public ImageOperationSuccessDTO(String message) {

        completed = message;
    }
}
