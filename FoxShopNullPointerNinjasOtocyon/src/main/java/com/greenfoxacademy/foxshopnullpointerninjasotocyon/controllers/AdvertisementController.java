package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.BadAttributeValueExpException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/advertisement")
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
     * @param page  page number for pagination. Cannot be null.
     * @param size  size of each page for pagination. Cannot be null.
     * @param categoryId  Optional, ID of the categories to filter advertisements. Can be null.
     * @param maxPrice  Optional, maximum price to filter advertisements. Can be null.
     *
     * @return  paginated list of Advertisements
     * @throws BadAttributeValueExpException  BadAttributeValueExpException If the provided page or size is null.
     */
    @GetMapping("/getAdvertisements")
    public Page<Advertisement> getAdvertisements(@RequestParam(required = false) Integer page,
                                                   @RequestParam(required = false) Integer size,
                                                   @RequestParam(required = false) Long categoryId,
                                                   @RequestParam(required = false) Integer maxPrice) throws BadAttributeValueExpException {

        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, size != null ? size : 10);
        return advertisementService.getAdvertisements(pageRequest, categoryId, maxPrice);
    }
}
