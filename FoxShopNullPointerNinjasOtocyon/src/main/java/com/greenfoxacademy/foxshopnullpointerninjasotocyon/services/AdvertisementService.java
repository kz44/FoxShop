package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckAdvertisement(AdvertisementDto advertisementDto);

    ResponseEntity<?> createNewAdvertisement(AdvertisementDto advertisementDto);

    ResponseEntity<?> addImageBinaryData(HttpServletRequest httpServletRequest, Long advertisementId);

    ResponseEntity<?> addImageBase64(String encodedImage, Long advertisementId);

    ResponseEntity<?> deleteImage(String imageUrl);
}
