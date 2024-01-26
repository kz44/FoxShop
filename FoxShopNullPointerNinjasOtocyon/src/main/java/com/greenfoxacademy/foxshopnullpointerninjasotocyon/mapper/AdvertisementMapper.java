package com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementPageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class AdvertisementMapper {


    /**
     * Converts an Advertisement entity to an AdvertisementPageableDTO.
     *
     * @param entity The Advertisement entity to be converted.
     * @return AdvertisementPageableDTO containing information from the Advertisement entity.
     */
    public AdvertisementPageableDTO toDTO(Advertisement entity) {
        return AdvertisementPageableDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .locationName(entity.getLocation().getName())
                .deliveryMethodName(entity.getDeliveryMethod().getName())
                .categoryName(entity.getCategory().getName())
                .conditionName(entity.getCondition().getName())
                .build();
    }

    /*      I think we won't use it, maybe it will be removed later

    public Advertisement toEntity (AdvertisementPageableDTO dto){
        return Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .location(Location.builder().name(dto.getLocationName()).build())
                .deliveryMethod(DeliveryMethod.builder().name(dto.getDeliveryMethodName()).build())
                .category(Category.builder().name(dto.getCategoryName()).build())
                .condition(Condition.builder().name(dto.getConditionName()).build())
                .build();
    }
     */


}
