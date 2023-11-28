package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementCreationDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private UserService userService;

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
}
