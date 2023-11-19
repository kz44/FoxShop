package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckNewAdvertisement(NewAdvertisementDto newAdvertisementDto);

    ResponseEntity<?> createNewAdvertisement(NewAdvertisementDto newAdvertisementDto);
}
