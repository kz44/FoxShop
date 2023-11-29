package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementPageableDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckAdvertisement(AdvertisementDto advertisementDto);

    ResponseEntity<?> createNewAdvertisement(AdvertisementDto advertisementDto);

    List<AdvertisementPageableDTO> getAdvertisements(Pageable pageable, Long categoryId, Integer maxPrice);

}
