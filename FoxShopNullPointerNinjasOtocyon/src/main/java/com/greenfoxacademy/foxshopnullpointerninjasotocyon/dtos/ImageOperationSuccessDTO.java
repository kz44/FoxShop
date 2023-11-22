package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageOperationSuccessDTO {

    private String completed;

    public ImageOperationSuccessDTO(String message) {

        completed = message;
    }
}
