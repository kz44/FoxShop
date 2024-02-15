package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdvertisementWithImageDTO {

    private Long id;

    private String title;

    private String description;

    private Integer price;

    private String locationName;

    private String deliveryMethodName;

    private String categoryName;

    private String conditionName;

    private List<String> images = new ArrayList<>();

}
