package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCreationDto {
    private String title;
    private String description;
    private Integer price;
    private Long locationId;
    private Long deliveryMethodId;
    private Long categoryId;
    private Long conditionId;

}
