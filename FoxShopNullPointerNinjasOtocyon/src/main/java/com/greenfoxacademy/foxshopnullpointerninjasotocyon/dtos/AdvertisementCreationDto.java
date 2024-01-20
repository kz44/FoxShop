package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AdvertisementCreationDto {
    private String title;
    private String description;
    private Integer price;
    private Long locationId;
    private Long deliveryMethodId;
    private Long categoryId;
    private Long conditionId;

}
