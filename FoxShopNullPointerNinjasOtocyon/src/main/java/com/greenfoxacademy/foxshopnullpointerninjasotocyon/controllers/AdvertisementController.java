package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private AdvertisementService advertisementService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewAdvertisement(@RequestBody(required = false) AdvertisementDto advertisementDto) {
        if (advertisementDto == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data for new advertisement."));
        }
        ResponseEntity<?> responseNullCheck = advertisementService.nullCheckAdvertisement(advertisementDto);
        if (!responseNullCheck.getStatusCode().is2xxSuccessful()) {
            return responseNullCheck;
        }
        return advertisementService.createNewAdvertisement(advertisementDto);
    }

    /**
     * This endpoint returns paginated list of Advertisements based on provided parameters
     *
     * Pageable contains tha page and size
     * @param categoryId Optional, ID of the categories to filter advertisements. Can be null.
     * @param maxPrice   Optional, maximum price to filter advertisements. Can be null.
     * @return paginated list of Advertisements
     * @throws BadAttributeValueExpException BadAttributeValueExpException If the provided page or size is null.
     */
    @GetMapping()
    public List<AdvertisementDto> getAdvertisements(Pageable pageable,
                                                    @RequestParam(required = false) Long categoryId,
                                                    @RequestParam(required = false) Integer maxPrice) throws IllegalArgumentException {
            return advertisementService.getAdvertisements(pageable, categoryId, maxPrice);
    }
}
