package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.PostImageDTO;
import org.springframework.http.ResponseEntity;

import java.io.InputStream;

public interface AdvertisementService {
    ResponseEntity<?> nullCheckNewAvertisement(NewAdvertisementDto newAdvertisementDto);

    ResponseEntity<?> createNewAdvertisement(NewAdvertisementDto newAdvertisementDto);

    ResponseEntity<?> addImageBase64(String decodedImage, Long id);

    ResponseEntity<?> nullCheckImageDto(PostImageDTO postImageDTO);

    ResponseEntity<?> addImageBinaryData(InputStream inputStream, Long advertisementId);
}
