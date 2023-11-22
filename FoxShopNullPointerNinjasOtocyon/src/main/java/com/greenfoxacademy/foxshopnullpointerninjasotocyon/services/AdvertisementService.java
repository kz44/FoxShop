package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckNewAvertisement(NewAdvertisementDto newAdvertisementDto);

    ResponseEntity<?> createNewAdvertisement(NewAdvertisementDto newAdvertisementDto);

//    ResponseEntity<?> addImageBinaryData(HttpServletRequest httpServletRequest, Long advertisementId,
//                                         String imageName);
//
//    ResponseEntity<?> addImageBase64(String decodedImage, HttpServletRequest httpServletRequest,
//                                     Long advertisementId, String imageName);

//    ResponseEntity<?> deleteImageEntity(String imageUrl, Long advertisementId);
}
