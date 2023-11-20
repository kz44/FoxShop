package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ImgSavedDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.PostImageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/advertisement")
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
     *  we keep HttpServletRequest in parameters:
     */

    @PostMapping("base64encoded/image/{imageName}/{advertisementId}")
    public ResponseEntity<?> addImageBase64(PostImageDTO postImageDTO, HttpServletRequest httpServletRequest,
                                            @PathVariable Long advertisementId, @PathVariable String imageName){
        if (postImageDTO == null){return ResponseEntity.badRequest().body(new ErrorMessageDTO("No data transfer file located."));}
        if (advertisementId == null || imageName == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement id or image name missing in path."));
        }
        return advertisementService.addImageBase64( postImageDTO.getImageBase64Encoded(),
                 httpServletRequest, advertisementId, imageName);
    }

    @PostMapping("binaryDataUpload/image/{imageName}/{advertisementId}")
    public ResponseEntity<?> uploadImageFromBinary(HttpServletRequest httpServletRequest,
                                                   @PathVariable Long advertisementId, @PathVariable String imageName) {
        if (advertisementId == null || imageName == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement id or image name missing in path."));
        }
        return advertisementService.addImageBinaryData(httpServletRequest, advertisementId, imageName);
    }
}
