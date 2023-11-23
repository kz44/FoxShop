package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * This endpoint close the advertisement by id
     * @param advertisementId id of the advertisement want to close
     *
     * @return
     *      - success message: if the advertisement closed
     *      - permission message: if the user don't have permission to close the advertisement
     *      - warning message: if the something went wrong during the changing advertisement status
     */
    @PostMapping("/closeAdvertisement/{advertisementId}")
    public ResponseEntity<String> closeAdvertisement(@PathVariable Long advertisementId) {
        try {
            if (advertisementService.closeAdvertisement(advertisementId)){
                return ResponseEntity.ok("Advertisement closed");
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to close this advertisement");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong while closing an advertisement");
        }
    }
}
