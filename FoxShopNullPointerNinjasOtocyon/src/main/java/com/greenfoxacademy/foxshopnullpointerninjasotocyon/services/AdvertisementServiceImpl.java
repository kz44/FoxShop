package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ImageOperationSuccessDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementResponseDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private AdvertisementRepository advertisementRepository;
    private LocationRepository locationRepository;
    private CategoryRepository categoryRepository;
    private ConditionRepository conditionRepository;
    private DeliveryMethodRepository deliveryMethodRepository;
    private UserRepository userRepository;
    private ImagePathRepository imagePathRepository;
    private JwtTokenService jwtTokenService;

    @Override
    public ResponseEntity<?> nullCheckNewAvertisement(NewAdvertisementDto newAdvertisementDto) {
        List<String> missingData = new ArrayList<>();
        if (newAdvertisementDto.getTitle() == null) {
            missingData.add("title");
        }
        if (newAdvertisementDto.getDescription() == null) {
            missingData.add("description");
        }
        if (newAdvertisementDto.getPrice() == null) {
            missingData.add("price");
        }
        if (newAdvertisementDto.getCategoryId() == null) {
            missingData.add("category id");
        }
        if (newAdvertisementDto.getConditionId() == null) {
            missingData.add("condition id");
        }
        if (newAdvertisementDto.getLocationId() == null) {
            missingData.add("location id");
        }
        if (newAdvertisementDto.getDeliveryMethodId() == null) {
            missingData.add("delivery method id");
        }
        if (!missingData.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingData)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> createNewAdvertisement(NewAdvertisementDto newAdvertisementDto) {
        List<String> errors = new ArrayList<>();
        Advertisement advertisement = new Advertisement();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        advertisement.setUser(user);
        advertisement.setTitle(newAdvertisementDto.getTitle());
        advertisement.setDescription(newAdvertisementDto.getDescription());
        advertisement.setPrice(newAdvertisementDto.getPrice());
        Optional<Category> category = categoryRepository.findById(newAdvertisementDto.getCategoryId());
        category.ifPresentOrElse(advertisement::setCategory, () -> errors.add("Wrong category id."));
        Optional<Condition> condition = conditionRepository.findById(newAdvertisementDto.getConditionId());
        condition.ifPresentOrElse(advertisement::setCondition, () -> errors.add("Wrong condition id."));
        Optional<Location> location = locationRepository.findById(newAdvertisementDto.getLocationId());
        location.ifPresentOrElse(advertisement::setLocation, () -> errors.add("Wrong location id."));
        Optional<DeliveryMethod> deliveryMethod = deliveryMethodRepository.findById(newAdvertisementDto.getDeliveryMethodId());
        deliveryMethod.ifPresentOrElse(advertisement::setDeliveryMethod, () -> errors.add("Wrong delivery method id."));
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        advertisementRepository.save(advertisement);
        return ResponseEntity.ok().body(new NewAdvertisementResponseDto(advertisement.getId()));
    }


    @Override
    @Transactional
    public ResponseEntity<?> addImageBase64(String encodedImage, HttpServletRequest httpServletRequest,
                                            Long advertisementId, String imageName) {
        if (encodedImage == null) {
            System.out.println("Encoded image is missing in data transfer object.");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Encoded image is missing in data transfer object."));
        }
        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
        if (!advertisement.isPresent()) {
            System.out.println("Advertisement entity not located in the database.");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement entity not located in the database."));
        }
        String token = jwtTokenService.resolveToken(httpServletRequest);
        //user model of the already authenticated user:
        User user = userRepository.findByUsername(jwtTokenService.parseJwt(token)).get();
        if (!advertisement.get().getUser().equals(user)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
        }
        String pathForSaving = new String();
        try {
            byte[] decodedImageBytes = Base64.getDecoder().decode(encodedImage); //decode String back to binary content:
            pathForSaving = inputBytesToImageFile(httpServletRequest, decodedImageBytes,
                    advertisementId, imageName);
        } catch (FileNotFoundException e) {
            System.out.println("File could not be constructed under the path specified.");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("File could not be constructed under the path specified."));
        } catch (IOException e) {
            System.out.println("Conversion of bytes into file failed.");
            ResponseEntity.badRequest().body(new ErrorMessageDTO("Conversion of bytes into file failed."));
        }

        ImagePath image = new ImagePath(pathForSaving);
        image.setAdvertisement(advertisement.get());
        advertisement.get().getImagePaths().add(image);
        advertisementRepository.save(advertisement.get());
        imagePathRepository.save(image);

        return ResponseEntity.ok(new ImageOperationSuccessDTO(pathForSaving));
    }

    @Override
    @Transactional
    public ResponseEntity<?> addImageBinaryData(HttpServletRequest httpServletRequest,
                                                Long advertisementId, String imageName) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
        if (!advertisement.isPresent()) {
            System.out.println("Advertisement entity not located in the database.");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement entity not located in the database."));
        }
        String token = jwtTokenService.resolveToken(httpServletRequest);
        //user model of the already authenticated user:
        User user = userRepository.findByUsername(jwtTokenService.parseJwt(token)).get();
        if (!advertisement.get().getUser().equals(user)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
        }
        String pathForSaving = new String();
        try {
            InputStream inputStream = httpServletRequest.getInputStream();
            if (inputStream.equals(InputStream.nullInputStream())) {
                System.out.println("Input stream in httpRequest is empty");
                return ResponseEntity.badRequest().body(new ErrorMessageDTO("Input stream in httpRequest is empty"));
            }
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            pathForSaving = inputBytesToImageFile(httpServletRequest, imageBytes,
                    advertisementId, imageName);
        } catch (FileNotFoundException e) {
            System.out.println("File could not be constructed under the path specified.");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("File could not be constructed under the path specified."));
        } catch (IOException e) {
            System.out.println("Conversion of bytes into file failed.");
            ResponseEntity.badRequest().body(new ErrorMessageDTO("Conversion of bytes into file failed."));
        }

        ImagePath image = new ImagePath(pathForSaving);
        image.setAdvertisement(advertisement.get());
        advertisement.get().getImagePaths().add(image);
        advertisementRepository.save(advertisement.get());
        imagePathRepository.save(image);

        return ResponseEntity.ok(new ImageOperationSuccessDTO(pathForSaving));
    }


//    private String inputBytesToImageFile(HttpServletRequest httpServletRequest, byte[] imageBytes,
//                                         Long advertisementId, String imageName)
//            throws IOException, FileNotFoundException {
//        String token = jwtTokenService.resolveToken(httpServletRequest);
////      as not specified otherwise, the controller endpoint is configured as accessible only for authenticated users
//        String username = jwtTokenService.parseJwt(token);
////      src/main/resources/assets/advertisementImages/<username>/<advertisement_id>/<image name>
//        String pathForSaving = "src/main/resources/assets/advertisementImages/"
//                + username + "/"
//                + advertisementId.toString() + "/"
//                + imageName + ".png";
//        File javaFileObject = new File(pathForSaving);
//        /* try creating file under the path specified - assuming directory+subdirectories exist already
//        if the directory tree is not fully existent yet, method: mkdirs(create all directories that do not exist yet)
//        and afterwards create the file
//         */
//        try {
//            FileOutputStream stream = new FileOutputStream(javaFileObject);
////          write bytes to result file:
//            stream.write(imageBytes);
//        } catch (FileNotFoundException fileNotFoundException) {
//            if (javaFileObject.getParentFile().mkdirs()) {
//                FileOutputStream stream = new FileOutputStream(javaFileObject);
//                stream.write(imageBytes);
//            } else {
//                System.out.println("Failed to create stream under directory " + javaFileObject.getParent());
//                throw new FileNotFoundException("Failed to create stream under directory " + javaFileObject.getParent());
//            }
//        }
//        return pathForSaving;
//    }



}