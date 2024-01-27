package com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementPageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementWithImageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.ImagePath;
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

    /**
     * Converts an Advertisement advertisement to an AdvertisementWithImageDTO.
     *
     * @param advertisement The Advertisement entity to be converted.
     * @return AdvertisementWithImageDTO containing information from the Advertisement entity.
     */
    public AdvertisementWithImageDTO advertisementToDtoWithImage(Advertisement advertisement) {
        AdvertisementWithImageDTO advertisementWithImageDTO = new AdvertisementWithImageDTO();
        advertisementWithImageDTO.setId(advertisement.getId());
        advertisementWithImageDTO.setTitle(advertisement.getTitle());
        advertisementWithImageDTO.setDescription(advertisement.getDescription());
        advertisementWithImageDTO.setPrice(advertisement.getPrice());
        advertisementWithImageDTO.setLocationName(advertisement.getLocation().getName());
        advertisementWithImageDTO.setDeliveryMethodName(advertisement.getDeliveryMethod().getName());
        advertisementWithImageDTO.setCategoryName(advertisement.getCategory().getName());
        advertisementWithImageDTO.setConditionName(advertisement.getCondition().getName());

        for (ImagePath imagePath : advertisement.getImagePaths()) {
            advertisementWithImageDTO.getImages().add("http://localhost:8080/api/advertisement/getImage?path=".concat(imagePath.getUrl()));
        }
        return advertisementWithImageDTO;
    }

}
