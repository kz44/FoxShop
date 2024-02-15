package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementCreationDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementPageableDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckAdvertisement(AdvertisementCreationDto advertisementCreationDto);

    ResponseEntity<?> createNewAdvertisement(AdvertisementCreationDto advertisementCreationDto);

    ResponseEntity<?> updateAdvertisement(Long id, AdvertisementCreationDto advertisementCreationDto);

    ResponseEntity<?> addImageBinaryData(HttpServletRequest httpServletRequest, Long advertisementId);

    ResponseEntity<?> addImageBase64(String encodedImage, Long advertisementId);

    ResponseEntity<?> deleteImage(String imageUrl);

    List<AdvertisementPageableDTO> getAdvertisements(Pageable pageable, Long categoryId, Integer maxPrice);

    ResponseEntity<?> closeAdvertisementById(Long advertisementId);

    ResponseEntity<?> getAdvertisementById(Long id);
}
