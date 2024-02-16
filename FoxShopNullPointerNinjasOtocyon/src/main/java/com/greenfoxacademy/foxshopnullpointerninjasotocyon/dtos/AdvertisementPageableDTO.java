package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Builder
@Data
@ToString
public class AdvertisementPageableDTO {
    private Long id;

    private String title;

    private String description;

    private Integer price;

    private String locationName;

    private String deliveryMethodName;

    private String categoryName;

    private String conditionName;
}
