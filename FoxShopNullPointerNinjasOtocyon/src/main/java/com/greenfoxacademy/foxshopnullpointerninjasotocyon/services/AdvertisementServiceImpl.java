package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementResponseDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ImageOperationSuccessDTO;
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

    /**
     * Checks for null values in the provided AdvertisementDto and returns an appropriate ResponseEntity.
     * <p>
     * This method examines the essential fields of the AdvertisementDto, ensuring none of them are null.
     * If any null values are found, it returns a ResponseEntity with a Bad Request status and an error message
     * indicating the missing data. Otherwise, it returns a ResponseEntity with an OK status.
     *
     * @param advertisementDto The AdvertisementDto to be checked for null values.
     * @return ResponseEntity<?> A ResponseEntity indicating the status of the null check.
     * - If null values are found, it returns a Bad Request status with an error message.
     * - If no null values are found, it returns an OK status.
     */
    @Override
    public ResponseEntity<?> nullCheckAdvertisement(AdvertisementDto advertisementDto) {
        List<String> missingData = new ArrayList<>();
        if (advertisementDto.getTitle() == null) {
            missingData.add("title");
        }
        if (advertisementDto.getDescription() == null) {
            missingData.add("description");
        }
        if (advertisementDto.getPrice() == null) {
            missingData.add("price");
        }
        if (advertisementDto.getCategoryId() == null) {
            missingData.add("category id");
        }
        if (advertisementDto.getConditionId() == null) {
            missingData.add("condition id");
        }
        if (advertisementDto.getLocationId() == null) {
            missingData.add("location id");
        }
        if (advertisementDto.getDeliveryMethodId() == null) {
            missingData.add("delivery method id");
        }
        if (!missingData.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingData)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new advertisement using the provided AdvertisementDto and associates it with the current authenticated user.
     * <p>
     * This method initializes a new Advertisement entity, sets the current user as its owner, and delegates to the
     * dataValidationAndSaveAdvertisement method for data validation and saving.
     *
     * @param advertisementDto The AdvertisementDto containing the information for the new advertisement.
     * @return ResponseEntity<?> A ResponseEntity containing either the saved Advertisement ID or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    @Override
    public ResponseEntity<?> createNewAdvertisement(AdvertisementDto advertisementDto) {
        Advertisement advertisement = new Advertisement();
        User user = getUserFromSecurityContextHolder();
        advertisement.setUser(user);
        return dataValidationAndSaveAdvertisement(advertisementDto, advertisement);
    }

    /**
     * Retrieves the currently authenticated user from the SecurityContextHolder.
     *
     * @return The User object associated with the authenticated user.
     */

    private User getUserFromSecurityContextHolder() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username).get();
    }

    /**
     * Performs data validation for the provided AdvertisementDto and updates the given Advertisement entity.
     * If validation passes, the updated Advertisement is saved to the repository.
     *
     * @param advertisementDto The AdvertisementDto containing the updated information for the advertisement.
     * @param advertisement    The Advertisement entity to be updated.
     * @return ResponseEntity<?> A ResponseEntity containing either the saved Advertisement ID or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    private ResponseEntity<?> dataValidationAndSaveAdvertisement(AdvertisementDto advertisementDto, Advertisement advertisement) {
        List<String> errors = new ArrayList<>();
        advertisement.setTitle(advertisementDto.getTitle());
        advertisement.setDescription(advertisementDto.getDescription());
        if (advertisementDto.getPrice() > 0) {
            advertisement.setPrice(advertisementDto.getPrice());
        } else {
            errors.add("Price must be positive number.");
        }
        Optional<Category> category = categoryRepository.findById(advertisementDto.getCategoryId());
        category.ifPresentOrElse(advertisement::setCategory, () -> errors.add("Wrong category id."));
        Optional<Condition> condition = conditionRepository.findById(advertisementDto.getConditionId());
        condition.ifPresentOrElse(advertisement::setCondition, () -> errors.add("Wrong condition id."));
        Optional<Location> location = locationRepository.findById(advertisementDto.getLocationId());
        location.ifPresentOrElse(advertisement::setLocation, () -> errors.add("Wrong location id."));
        Optional<DeliveryMethod> deliveryMethod = deliveryMethodRepository.findById(advertisementDto.getDeliveryMethodId());
        deliveryMethod.ifPresentOrElse(advertisement::setDeliveryMethod, () -> errors.add("Wrong delivery method id."));
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        advertisementRepository.save(advertisement);
        return ResponseEntity.ok().body(new AdvertisementResponseDto(advertisement.getId()));
    }


//    @Override
//    @Transactional
//    public ResponseEntity<?> addImageBase64(String encodedImage, HttpServletRequest httpServletRequest,
//                                            Long advertisementId) {
//        if (encodedImage == null) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Encoded image is missing in data transfer object."));
//        }
//        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
//        if (!advertisement.isPresent()) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement entity not located in the database."));
//        }
//        String token = jwtTokenService.resolveToken(httpServletRequest);
//        //user model of the already authenticated user:
//        User user = userRepository.findByUsername(jwtTokenService.parseJwt(token)).get();
//        if (!advertisement.get().getUser().equals(user)) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
//        }
//        String pathForSaving = new String();
//        try {
//            byte[] decodedImageBytes = Base64.getDecoder().decode(encodedImage); //decode String back to binary content:
//            pathForSaving = inputBytesToImageFile(httpServletRequest, decodedImageBytes,
//                    advertisement.get());
//        } catch (FileNotFoundException e) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("File could not be constructed under the path specified."));
//        } catch (IOException e) {
//            ResponseEntity.badRequest().body(new ErrorMessageDTO("Conversion of bytes into file failed."));
//        }
//
//        ImagePath image = new ImagePath(pathForSaving);
//        image.setAdvertisement(advertisement.get());
//        advertisement.get().getImagePaths().add(image);
//        advertisementRepository.save(advertisement.get());
//        imagePathRepository.save(image);
//
//        return ResponseEntity.ok(new ImageOperationSuccessDTO(pathForSaving));
//    }

//    @Override
//    @Transactional
//    public ResponseEntity<?> addImageBinaryData(HttpServletRequest httpServletRequest,
//                                                Long advertisementId) {
//        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
//        if (!advertisement.isPresent()) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement entity not located in the database."));
//        }
//        String token = jwtTokenService.resolveToken(httpServletRequest);
//        //user model of the already authenticated user:
//        User user = userRepository.findByUsername(jwtTokenService.parseJwt(token)).get();
//        if (!advertisement.get().getUser().equals(user)) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
//        }
//        String pathForSaving = new String();
//        try {
//            InputStream inputStream = httpServletRequest.getInputStream();
//            byte[] imageBytes = IOUtils.toByteArray(inputStream);
//            if (imageBytes.length == 0) {
//                return ResponseEntity.badRequest().body(new ErrorMessageDTO("The submitted http request does not contain any image binary data"));
//            }
//            pathForSaving = inputBytesToImageFile(httpServletRequest, imageBytes,
//                    advertisement.get());
//        } catch (FileNotFoundException e) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("File could not be constructed under the path specified."));
//        } catch (IOException e) {
//            ResponseEntity.badRequest().body(new ErrorMessageDTO("Conversion of bytes into file failed."));
//        }
//
//        ImagePath image = new ImagePath(pathForSaving);
//        image.setAdvertisement(advertisement.get());
//        advertisement.get().getImagePaths().add(image);
//        advertisementRepository.save(advertisement.get());
//        imagePathRepository.save(image);
//
//        return ResponseEntity.ok(new ImageOperationSuccessDTO(pathForSaving));
//    }


    private String inputBytesToImageFile(HttpServletRequest httpServletRequest, byte[] imageBytes,
                                         Advertisement advertisementEntity)
            throws IOException, FileNotFoundException {
        String token = jwtTokenService.resolveToken(httpServletRequest);
//      as not specified otherwise, the controller endpoint is configured as accessible only for authenticated users
        String username = jwtTokenService.parseJwt(token);
//      src/main/resources/assets/advertisementImages/<username>/<advertisement_id>/<image number>
        String pathForSaving = "src/main/resources/assets/advertisementImages/"
                + username + "/"
                + advertisementEntity.getId().toString() + "/"
                + numberForNewImageEntity + ".png";
        File javaFileObject = new File(pathForSaving);
        /* try creating file under the path specified - assuming directory+subdirectories exist already
        if the directory tree is not fully existent yet, method: mkdirs(create all directories that do not exist yet)
        and afterwards create the file
         */
        try {
            FileOutputStream stream = new FileOutputStream(javaFileObject);
//          write bytes to result file:
            stream.write(imageBytes);
        } catch (FileNotFoundException fileNotFoundException) {
            if (javaFileObject.getParentFile().mkdirs()) {
                FileOutputStream stream = new FileOutputStream(javaFileObject);
                stream.write(imageBytes);
            } else {
                throw new FileNotFoundException("Failed to create stream under directory " + javaFileObject.getParent());
            }
        }
        return pathForSaving;
    }

    private Integer extractImageNumberFromUrl(String url) {
        int beginIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf(".");
        String imageNumberString = url.substring(beginIndex, endIndex);
       return Integer.parseInt(imageNumberString);
    }

//    @Override
//    @Transactional
//    public ResponseEntity<?> deleteImageEntity(HttpServletRequest httpServletRequest, String imageUrl, Long advertisementId) {
//        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
//        Optional<ImagePath> imagePath = imagePathRepository.findDistinctByUrl(imageUrl);
//        if (advertisement.isEmpty()) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement not located in database."));
//        }
//        String token = jwtTokenService.resolveToken(httpServletRequest);
//        //user model of the already authenticated user:
//        User user = userRepository.findByUsername(jwtTokenService.parseJwt(token)).get();
//        if (!advertisement.get().getUser().equals(user)) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
//        }
//        if (imagePath.isEmpty()) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Image not located in database."));
//        }
//        if (!advertisement.get().getImagePaths().contains(imagePath.get())) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement does not contain the image path specified."));
//        }
////       remove static file from directory:
//        try {
//            boolean deletionStaticFileResult = deleteImageFile(imageUrl);
//        } catch (IOException ioException) {
//            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The image file does not exist under the path specified."));
//        }
//        advertisement.get().getImagePaths().remove(imagePath.get());
//        advertisementRepository.save(advertisement.get());
//        imagePathRepository.delete(imagePath.get());
//        return ResponseEntity.ok(new ImageOperationSuccessDTO("Image path successfully removed from advertisement."));
//    }

    private boolean deleteImageFile(String imageUrl) throws IOException {
        File imageFileToBeDeleted = new File(imageUrl);
        if (!imageFileToBeDeleted.exists()) {
            throw new IOException();
        }
        return imageFileToBeDeleted.delete();
    }

}