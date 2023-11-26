package com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class AdvertisementMapper {


    public AdvertisementDto entityToDTO (Advertisement entity){
        return AdvertisementDto.builder()
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .locationId(entity.getLocation().getId())
                .deliveryMethodId(entity.getDeliveryMethod().getId())
                .categoryId(entity.getCategory().getId())
                .conditionId(entity.getCondition().getId())
                .build();
    }

    public Advertisement DTOtoEntity (AdvertisementDto dto){
        return Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .location(Location.builder().id(dto.getLocationId()).build())
                .deliveryMethod(DeliveryMethod.builder().id(dto.getDeliveryMethodId()).build())
                .category(Category.builder().id(dto.getCategoryId()).build())
                .condition(Condition.builder().id(dto.getConditionId()).build())
                .build();
    }



}
