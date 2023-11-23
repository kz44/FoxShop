package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckAdvertisement(AdvertisementDto advertisementDto);

    ResponseEntity<?> createNewAdvertisement(AdvertisementDto advertisementDto);
    
}
