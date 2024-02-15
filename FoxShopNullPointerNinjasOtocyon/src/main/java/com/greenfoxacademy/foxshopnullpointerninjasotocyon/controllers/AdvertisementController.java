package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

    /**
     * instead of method parameter HttpServletRequest in addImage endpoints,
     * InputStream is a spring-automatically-created alternative, where the conversion
     * into InputStream does not have to be done manually in the method body.
     * However, as HttpServletRequest is used also to extract token -> username,
     * we keep HttpServletRequest in parameters:
     */

 /* (For testing) base64 image online encoder gives out an encoded string needed for the PostImageDTO's field: 'imageBase64Encoded'
   https://codebeautify.org/image-to-base64-converter
   */
    @PostMapping(value = {
            "/base64encoded/image", "/base64encoded/image/",
            "/base64encoded/image/{advertisementId}"})

    public ResponseEntity<?> addImageBase64(@RequestBody(required = false) PostImageDTO postImageDTO,
                                            @PathVariable(required = false) Long advertisementId) {
        if (postImageDTO == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("No data transfer file provided."));
        }
        if (advertisementId == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement id missing in request path."));
        }
        return advertisementService.addImageBase64(postImageDTO.getImageBase64Encoded(), advertisementId);
    }

    @PostMapping(value = {
            "/binaryDataUpload/image", "/binaryDataUpload/image/",
            "/binaryDataUpload/image/{advertisementId}"})
    public ResponseEntity<?> uploadImageFromBinary(HttpServletRequest httpServletRequest,
                                                   @PathVariable(required = false) Long advertisementId) {
        if (advertisementId == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(
                    "Advertisement id missing in request path."));
        }
        return advertisementService.addImageBinaryData(httpServletRequest, advertisementId);
    }

    @DeleteMapping("/removeImage")
    public ResponseEntity<?> removeImageFromAdvertisement(@RequestBody(required = false) RemoveImageDTO removeImageDto) {
        if (removeImageDto == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(
                    "Removal data have not been attached to the request."));
        }
        return advertisementService.deleteImage(removeImageDto.getImageUrl());
    }

    /**
     * This endpoint returns paginated list of AdvertisementDto based on provided parameters
     * <p>
     * Pageable contains tha page and size value
     *
     * @param categoryId Optional, ID of the categories to filter advertisements. Can be null.
     * @param maxPrice   Optional, maximum price to filter advertisements. Can be null.
     * @return paginated list of Advertisements
     */
    @GetMapping("/advertisements")
    public List<AdvertisementPageableDTO> getAdvertisements(Pageable pageable,
                                                            @RequestParam(required = false) Long categoryId,
                                                            @RequestParam(required = false) Integer maxPrice) {
        return advertisementService.getAdvertisements(pageable, categoryId, maxPrice);
    }


    /**
     * This endpoint close the advertisement by id
     *
     * @param advertisementId id of the advertisement want to close
     * @return - success message: if the advertisement closed
     * - success message: if tha advertisement closed by ADMIN
     * - error message: if the advertisement is already closed
     * - error message: if the user don't have permission to close the advertisement
     * - error message: if the something went wrong during the changing advertisement status
     */
    @PostMapping(value = {
            "/closeAdvertisement/{advertisementId}",
            "/closeAdvertisement/",
            "/closeAdvertisement"
    })
    public ResponseEntity<?> closeAdvertisement(@PathVariable(required = false) Long advertisementId) {
        return advertisementService.closeAdvertisementById(advertisementId);
    }

    @GetMapping(value = {"/{id}", "/"})
    public ResponseEntity<?> getAdvertisementById(@PathVariable(required = false) Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Please provide the ID of the advertisement."));
        }
        return advertisementService.getAdvertisementById(id);
    }

    @GetMapping("/getImage")
    public ResponseEntity<?> getImage(@RequestParam(required = false) String path) throws IOException {
        if (path == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The path of the image is missing."));
        }
        Path imagePath = Path.of(path);
        if (!Files.exists(imagePath)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is no picture on provided path."));
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(Files.readAllBytes(imagePath));
    }
}
