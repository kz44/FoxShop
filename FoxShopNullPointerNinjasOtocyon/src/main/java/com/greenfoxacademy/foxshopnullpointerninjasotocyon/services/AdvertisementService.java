package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementCreationDto;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckAdvertisement(AdvertisementCreationDto advertisementCreationDto);
   
    ResponseEntity<?> createNewAdvertisement(AdvertisementCreationDto advertisementCreationDto);

    ResponseEntity<?> updateAdvertisement(Long id, AdvertisementCreationDto advertisementCreationDto);
    
    ResponseEntity<?> closeAdvertisementById(Long advertisementId);
}
