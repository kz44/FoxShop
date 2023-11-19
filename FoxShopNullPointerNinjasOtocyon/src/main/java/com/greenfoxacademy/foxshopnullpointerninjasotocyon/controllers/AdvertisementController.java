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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

    @PostMapping(value = "/classicHtmlFormUpload/image/{advertisementId}", consumes = {"image/png", "image/jpeg"})
    public ResponseEntity<?> uploadImageFromFormToAdvertisement(@RequestParam("file") MultipartFile mutlipartFile, @PathVariable Long advertisementId) {
        String filename = "testFileName.jpg";
        String pathForSaving = "src/main/resources/assets/advertisementImages" + File.pathSeparator + filename;
        File javaFileObject = new File(pathForSaving);
        try {
            byte[] imageBytes = mutlipartFile.getBytes();
            try (FileOutputStream stream = new FileOutputStream(javaFileObject)) {//write decodedImageBytes to outputFile:
                stream.write(imageBytes);
            } catch (FileNotFoundException e) {
                System.out.println("File could not be constructed under the path specified.");
                return ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));
            }
        } catch (IOException e) {
            System.out.println("Writing bytes into file failed.");
            ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));
        }
        return ResponseEntity.ok(new ImgSavedDTO(pathForSaving));
    }

    @PostMapping(value = "binaryDataUpload/image/{advertisementId}")
    public ResponseEntity<?> uploadImageFromBinary(HttpServletRequest httpServletRequest, @PathVariable Long advertisementId) throws Exception {
        if (advertisementId == null){
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement id missing in path."));
        }
        String filename = "testFileName.jpg";
        String pathForSaving = "src/main/resources/assets/advertisementImages" + File.pathSeparator + filename;
        File javaFileObject = new File(pathForSaving);
        try {
            byte[] imageBytes = IOUtils.toByteArray(httpServletRequest.getInputStream());
            try (FileOutputStream stream = new FileOutputStream(javaFileObject)) {//write decodedImageBytes to outputFile:
                stream.write(imageBytes);
            } catch (FileNotFoundException e) {
                System.out.println("File could not be constructed under the path specified.");
                return ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));
            }
        } catch (IOException e) {
            System.out.println("Writing bytes into file failed.");
            ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));
        }

        return ResponseEntity.ok(new ImgSavedDTO(pathForSaving));
    }
}
