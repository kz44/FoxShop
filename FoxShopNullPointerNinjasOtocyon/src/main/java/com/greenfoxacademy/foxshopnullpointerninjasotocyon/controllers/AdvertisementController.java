package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementCreationDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

    private AdvertisementService advertisementService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewAdvertisement(@RequestBody(required = false) AdvertisementCreationDto advertisementCreationDto) {
        if (advertisementCreationDto == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data for new advertisement."));
        }
        ResponseEntity<?> responseNullCheck = advertisementService.nullCheckAdvertisement(advertisementCreationDto);
        if (!responseNullCheck.getStatusCode().is2xxSuccessful()) {
            return responseNullCheck;
        }
        return advertisementService.createNewAdvertisement(advertisementCreationDto);
    }

    @PutMapping(value = {"/update/{id}", "/update/", "/update"})
    public ResponseEntity<?> updateAdvertisement(@PathVariable(required = false) Long id, @RequestBody(required = false) AdvertisementCreationDto advertisementCreationDto) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("In the path there is missing the id of advertisement to be updated."));
        }
        if (advertisementCreationDto == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data for update advertisement."));
        }
        ResponseEntity<?> responseNullCheck = advertisementService.nullCheckAdvertisement(advertisementCreationDto);
        if (!responseNullCheck.getStatusCode().is2xxSuccessful()) {
            return responseNullCheck;
        }
        return advertisementService.updateAdvertisement(id, advertisementCreationDto);
    }
}
