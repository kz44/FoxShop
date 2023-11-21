package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImgSavedDTO {

    private String success;

    public ImgSavedDTO(String pathForSaving) {

        success = "File saved succesfully under: " + pathForSaving;
    }
}
