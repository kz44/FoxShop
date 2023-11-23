package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.PostImageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/advertisement")
public class AdvertisementController {

    private AdvertisementService advertisementService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewAdvertisement(@RequestBody(required = false) NewAdvertisementDto newAdvertisementDto) {
        if (newAdvertisementDto == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data for new advertisement."));
        }
        ResponseEntity<?> responseNullCheck = advertisementService.nullCheckNewAvertisement(newAdvertisementDto);
        if (!responseNullCheck.getStatusCode().is2xxSuccessful()) {
            return responseNullCheck;
        }
        return advertisementService.createNewAdvertisement(newAdvertisementDto);
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
            "/base64encoded/image","/base64encoded/image/",
            "/base64encoded/image/{advertisementId}"})

    public ResponseEntity<?> addImageBase64(@RequestBody(required = false) PostImageDTO postImageDTO, HttpServletRequest httpServletRequest,
                                            @PathVariable(required = false) Long advertisementId) {
        if (postImageDTO == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("No data transfer file provided."));
        }
        if (advertisementId == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement id missing in request path."));
        }
        return advertisementService.addImageBase64(postImageDTO.getImageBase64Encoded(),
                httpServletRequest, advertisementId);
    }

    @PostMapping(value = {
            "/binaryDataUpload/image","/binaryDataUpload/image/",
            "/binaryDataUpload/image/{advertisementId}"})
    public ResponseEntity<?> uploadImageFromBinary(HttpServletRequest httpServletRequest,
                                                   @PathVariable(required = false) Long advertisementId) {
        if (advertisementId == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(
                    "Advertisement id missing in request path."));
        }
        return advertisementService.addImageBinaryData(httpServletRequest, advertisementId);
    }


}

