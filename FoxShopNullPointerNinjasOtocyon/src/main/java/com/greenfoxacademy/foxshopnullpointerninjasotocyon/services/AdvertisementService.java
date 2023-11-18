package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.PostImageDTO;
import org.springframework.http.ResponseEntity;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckNewAvertisement(NewAdvertisementDto newAdvertisementDto);

    ResponseEntity<?> createNewAdvertisement(NewAdvertisementDto newAdvertisementDto);

    ResponseEntity<?> addImage(String decodedImage, Long id);

    ResponseEntity<?> nullCheckImageDto(PostImageDTO postImageDTO);
}
