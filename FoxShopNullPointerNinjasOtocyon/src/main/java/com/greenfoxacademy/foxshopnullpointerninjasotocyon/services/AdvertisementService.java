package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementCreationDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckAdvertisement(AdvertisementCreationDto advertisementCreationDto);

    ResponseEntity<?> createNewAdvertisement(AdvertisementCreationDto advertisementCreationDto);

    ResponseEntity<?> updateAdvertisement(Long id, AdvertisementCreationDto advertisementCreationDto);

//    ResponseEntity<?> addImageBinaryData(HttpServletRequest httpServletRequest, Long advertisementId);
//
//    ResponseEntity<?> addImageBase64(String encodedImage, Long advertisementId);

    ResponseEntity<?> deleteImage(String imageUrl);
}
