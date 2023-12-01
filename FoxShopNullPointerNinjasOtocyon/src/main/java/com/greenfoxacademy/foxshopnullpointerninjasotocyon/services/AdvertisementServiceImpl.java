package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.mapper.AdvertisementMapper;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdvertisementServiceImpl implements AdvertisementService {

    private final UserServiceImpl userServiceImpl;
    private final AdvertisementMapper advertisementMapper;
    private AdvertisementRepository advertisementRepository;
    private LocationRepository locationRepository;
    private CategoryRepository categoryRepository;
    private ConditionRepository conditionRepository;
    private DeliveryMethodRepository deliveryMethodRepository;
    private UserService userService;
    private ImagePathRepository imagePathRepository;

    /**
     * Checks for null values in the provided AdvertisementDto and returns an appropriate ResponseEntity.
     * <p>
     * This method examines the essential fields of the AdvertisementDto, ensuring none of them are null.
     * If any null values are found, it returns a ResponseEntity with a Bad Request status and an error message
     * indicating the missing data. Otherwise, it returns a ResponseEntity with an OK status.
     *
     * @param advertisementCreationDto The AdvertisementDto to be checked for null values.
     * @return ResponseEntity<?> A ResponseEntity indicating the status of the null check.
     * - If null values are found, it returns a Bad Request status with an error message.
     * - If no null values are found, it returns an OK status.
     */
    @Override
    public ResponseEntity<?> nullCheckAdvertisement(AdvertisementCreationDto advertisementCreationDto) {
        List<String> missingData = new ArrayList<>();
        if (advertisementCreationDto.getTitle() == null) {
            missingData.add("title");
        }
        if (advertisementCreationDto.getDescription() == null) {
            missingData.add("description");
        }
        if (advertisementCreationDto.getPrice() == null) {
            missingData.add("price");
        }
        if (advertisementCreationDto.getCategoryId() == null) {
            missingData.add("category id");
        }
        if (advertisementCreationDto.getConditionId() == null) {
            missingData.add("condition id");
        }
        if (advertisementCreationDto.getLocationId() == null) {
            missingData.add("location id");
        }
        if (advertisementCreationDto.getDeliveryMethodId() == null) {
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
     * @param advertisementCreationDto The AdvertisementDto containing the information for the new advertisement.
     * @return ResponseEntity<?> A ResponseEntity containing either the saved Advertisement ID or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    @Override
    public ResponseEntity<?> createNewAdvertisement(AdvertisementCreationDto advertisementCreationDto) {
        Advertisement advertisement = new Advertisement();
        User user = userService.getUserFromSecurityContextHolder();
        advertisement.setUser(user);

        return dataValidationAndSaveAdvertisement(advertisementCreationDto, advertisement, true);
    }

    /**
     * This method returns a paginated list of Advertisements where you can add some filters but not necessary
     *
     * @param pageable   contains the page and size values for pagination
     * @param categoryId Optional, ID of the categories to filter advertisements. Can be null.
     * @param maxPrice   Optional, maximum price to filter advertisements. Can be null.
     * @return paginated list of Advertisements
     */
    @Override
    public List<AdvertisementPageableDTO> getAdvertisements(Pageable pageable, Long categoryId, Integer maxPrice) {
        return advertisementRepository.searchAdvertisements(categoryId, maxPrice, pageable).stream().map(advertisementMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Performs data validation for the provided AdvertisementDto and updates the given Advertisement entity.
     * If validation passes, the updated Advertisement is saved to the repository.
     *
     * @param advertisementCreationDto The AdvertisementDto containing the updated information for the advertisement.
     * @param advertisement            The Advertisement entity to be updated.
     * @return ResponseEntity<?> A ResponseEntity containing either the saved Advertisement ID or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    private ResponseEntity<?> dataValidationAndSaveAdvertisement(AdvertisementCreationDto advertisementCreationDto, Advertisement advertisement, boolean createNew) {
        List<String> errors = new ArrayList<>();
        advertisement.setTitle(advertisementCreationDto.getTitle());
        advertisement.setDescription(advertisementCreationDto.getDescription());
        if (advertisementCreationDto.getPrice() > 0) {
            advertisement.setPrice(advertisementCreationDto.getPrice());
        } else {
            errors.add("Price must be positive number.");
        }
        Optional<Category> category = categoryRepository.findById(advertisementCreationDto.getCategoryId());
        category.ifPresentOrElse(advertisement::setCategory, () -> errors.add("Wrong category id."));
        Optional<Condition> condition = conditionRepository.findById(advertisementCreationDto.getConditionId());
        condition.ifPresentOrElse(advertisement::setCondition, () -> errors.add("Wrong condition id."));
        Optional<Location> location = locationRepository.findById(advertisementCreationDto.getLocationId());
        location.ifPresentOrElse(advertisement::setLocation, () -> errors.add("Wrong location id."));
        Optional<DeliveryMethod> deliveryMethod = deliveryMethodRepository.findById(advertisementCreationDto.getDeliveryMethodId());
        deliveryMethod.ifPresentOrElse(advertisement::setDeliveryMethod, () -> errors.add("Wrong delivery method id."));
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        advertisementRepository.save(advertisement);
        String message = String.format("Your advertisement with id %d was successfully %s.", advertisement.getId(), (createNew ? "created" : "updated"));
        return ResponseEntity.ok().body(new SuccessMessageDTO(message));
    }

    /**
     * Updates an existing advertisement with the provided AdvertisementDto.
     * <p>
     * This method retrieves the current user's authentication information, validates ownership of the advertisement,
     * and then proceeds to update the advertisement based on the provided AdvertisementDto.
     *
     * @param id                       The unique identifier of the advertisement to be updated.
     * @param advertisementCreationDto The AdvertisementDto containing the updated information for the advertisement.
     * @return ResponseEntity<?> A ResponseEntity containing either the updated Advertisement or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    @Override
    public ResponseEntity<?> updateAdvertisement(Long id, AdvertisementCreationDto advertisementCreationDto) {
        User user = userService.getUserFromSecurityContextHolder();
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(id);
        if (advertisementOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is no advertisement with this id."));
        }
        Advertisement advertisement = advertisementOptional.get();
        if (!advertisement.getUser().equals(user)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
        }
        return dataValidationAndSaveAdvertisement(advertisementCreationDto, advertisement, false);
    }


    @Override
    @Transactional
    public ResponseEntity<?> addImageBase64(String encodedImage, Long advertisementId) {
        if (encodedImage == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Encoded image is missing in data transfer object."));
        }
        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
        if (!advertisement.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement entity not located in the database."));
        }
        //user model of the already authenticated user:
        // the controller endpoint is configured as accessible only for authenticated users
        User user = userService.getUserFromSecurityContextHolder();
        String username = user.getUsername();
        if (!advertisement.get().getUser().equals(user)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
        }
        String pathForSaving = new String();
        try {
            byte[] decodedImageBytes = Base64.getDecoder().decode(encodedImage); //decode String back to binary content:
            pathForSaving = inputBytesToImageFile(username, decodedImageBytes,
                    advertisement.get());
        } catch (FileNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("File could not be constructed under the path specified."));
        } catch (IOException e) {
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
                                                Long advertisementId) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
        if (!advertisement.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement entity not located in the database."));
        }
        User user = userService.getUserFromSecurityContextHolder();
        String username = user.getUsername();
        if (!advertisement.get().getUser().equals(user)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
        }
        String pathForSaving = new String();
        try {
            InputStream inputStream = httpServletRequest.getInputStream();
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            if (imageBytes.length == 0) {
                return ResponseEntity.badRequest().body(new ErrorMessageDTO("The submitted http request does not contain any image binary data"));
            }
            pathForSaving = inputBytesToImageFile(username, imageBytes,
                    advertisement.get());
        } catch (FileNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("File could not be constructed under the path specified."));
        } catch (IOException e) {
            ResponseEntity.badRequest().body(new ErrorMessageDTO("Conversion of bytes into file failed."));
        }

        ImagePath image = new ImagePath(pathForSaving);
        image.setAdvertisement(advertisement.get());
        advertisement.get().getImagePaths().add(image);
        advertisementRepository.save(advertisement.get());
        imagePathRepository.save(image);

        return ResponseEntity.ok(new ImageOperationSuccessDTO(pathForSaving));
    }

    private String inputBytesToImageFile(String username, byte[] imageBytes,
                                         Advertisement advertisementEntity)
            throws IOException, FileNotFoundException {
        Optional<Integer> advertisementMaximumImageNumber = advertisementEntity.getImagePaths().stream()
                .map(x -> extractImageNumberFromUrl(x.getUrl())).max(Integer::compareTo);
        int numberForNewImageEntity = 0;
        if (advertisementMaximumImageNumber.isPresent()) {
            numberForNewImageEntity = (advertisementMaximumImageNumber.get().intValue() + 1);
        }
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

    @Override
    @Transactional
    public ResponseEntity<?> deleteImage(String imageUrl) {
        if (imageUrl == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(
                    "The submitted request needs to contain the image url."));
        }

        Long advertisementId = extractAdvertisementIdFromUrl(imageUrl);
        Optional<Advertisement> advertisement = advertisementRepository.findById(advertisementId);
        Optional<ImagePath> imagePath = imagePathRepository.findDistinctByUrl(imageUrl);
        if (advertisement.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement not located in database."));
        }
        //user model of the already authenticated user:
        User user = userService.getUserFromSecurityContextHolder();
        if (!advertisement.get().getUser().equals(user)) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("It is not possible to change another user's advertisement."));
        }
        if (imagePath.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Image not located in database."));
        }
        if (!advertisement.get().getImagePaths().contains(imagePath.get())) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Advertisement does not contain the image path specified."));
        }
//          //   remove static file from directory:
        try {
            boolean deletionStaticFileResult = deleteImageFile(imageUrl);
        } catch (IOException ioException) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The image file does not exist under the path specified."));
        }
        advertisement.get().getImagePaths().remove(imagePath.get());
        advertisementRepository.save(advertisement.get());
        imagePathRepository.delete(imagePath.get());
        return ResponseEntity.ok(new ImageOperationSuccessDTO("Image path successfully removed from advertisement."));
    }

    private Integer extractImageNumberFromUrl(String url) {
        int beginIndex = url.lastIndexOf("/") + 1;
        int endIndex = url.lastIndexOf(".");
        String imageNumberString = url.substring(beginIndex, endIndex);
        return Integer.parseInt(imageNumberString);
    }

    private Long extractAdvertisementIdFromUrl(String url) {
        String[] urlParts = url.split("/");
        int beginIndex = url.indexOf(urlParts[6]);
        int endIndex = url.lastIndexOf("/");
        String imageNumberString = url.substring(beginIndex, endIndex);
        return Long.parseLong(imageNumberString);
    }

    private boolean deleteImageFile(String imageUrl) throws IOException {
        File imageFileToBeDeleted = new File(imageUrl);
        if (!imageFileToBeDeleted.exists()) {
            throw new IOException();
        }
        return imageFileToBeDeleted.delete();
    }

    /**
     * Closes the specified advertisement identified by its unique identifier.
     *
     * @param advertisementId The unique identifier of the advertisement to be closed.
     * @return ErrorMessageDTO: if the advertisement id was invalid.
     *          SuccessMessageDTO: if the advertisement already closed
     *          SuccessMessageDTO: if the advertisement closed by ADMIN
     *          SuccessMessageDTO: if the advertisement closed
     *          ErrorMessageDTO: if the advertisement cannot be closed because user don't have permission to do that
     */
    @Override
    public ResponseEntity<?> closeAdvertisementById(Long advertisementId) {

        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(advertisementId);

        if (advertisementId == null || advertisementId <= 0 || advertisementOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Invalid advertisement ID"));
        }

        Advertisement advertisement = advertisementOptional.get();

        if (advertisement.isClosed()) {
            return ResponseEntity.ok().body(new SuccessMessageDTO("Advertisement is already closed"));
        }

        User loggedUser = userService.getUserFromSecurityContextHolder();

        if (userService.checkUserRole().equals("ADMIN")) {
            advertisement.setClosed(true);
            advertisementRepository.save(advertisement);
            return ResponseEntity.ok().body(new SuccessMessageDTO("Advertisement is closed by ADMIN"));
        }

        if (advertisement.getUser().getUsername().equals(loggedUser.getUsername())) {
            advertisement.setClosed(true);
            advertisementRepository.save(advertisement);
            return ResponseEntity.ok().body(new SuccessMessageDTO("Advertisement is closed"));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMessageDTO("You don't have permission to close this advertisement"));
        }
    }

}