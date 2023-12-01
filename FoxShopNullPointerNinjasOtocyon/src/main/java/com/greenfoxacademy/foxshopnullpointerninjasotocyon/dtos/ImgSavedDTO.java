package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImgSavedDTO {

    private String succesfullySaved;

    public ImgSavedDTO(String pathForSaving) {

        succesfullySaved = "File saved succesfully under: " + pathForSaving;
    }
}
