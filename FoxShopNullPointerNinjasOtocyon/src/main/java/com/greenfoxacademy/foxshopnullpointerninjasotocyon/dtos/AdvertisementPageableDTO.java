package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class AdvertisementPageableDTO {
    private String title;

    private String description;

    private Integer price;

    private String locationName;

    private String deliveryMethodName;

    private String categoryName;

    private String conditionName;
}
