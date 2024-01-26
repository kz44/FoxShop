package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.ImagePath;
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

    public AdvertisementWithImageDTO(Advertisement advertisement) {
        this.id = advertisement.getId();
        this.title = advertisement.getTitle();
        this.description = advertisement.getDescription();
        this.price = advertisement.getPrice();
        this.locationName = advertisement.getLocation().getName();
        this.deliveryMethodName = advertisement.getDeliveryMethod().getName();
        this.categoryName = advertisement.getCategory().getName();
        this.conditionName = advertisement.getCondition().getName();
        for (ImagePath imagePath : advertisement.getImagePaths()) {
            images.add("http://localhost:8080/api/advertisement/getImage?path=".concat(imagePath.getUrl()));
        }
    }
}
