package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ImgSavedDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
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
     * InputStream is the spring-automatically-created alternative in parameters, instead of: HttpServletRequest httpServletRequest
     * From HttpServletRequest, we would otherwise manually create InputStream in our code:
     * try {
     * InputStream inputStream = httpServletRequest.getInputStream();
     * } catch (IOException e) {
     * System.out.println("Writing bytes into file failed.");
     * }
     */

    @PostMapping(value = "binaryDataUpload/image/{advertisementId}")
    public ResponseEntity<?> uploadImageFromBinary(InputStream inputStream, @PathVariable Long advertisementId) {
        if (advertisementId == null || inputStream.equals(InputStream.nullInputStream())) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement id missing in path."));
        }
        return advertisementService.addImageBinaryData(inputStream, advertisementId);
    }
}
