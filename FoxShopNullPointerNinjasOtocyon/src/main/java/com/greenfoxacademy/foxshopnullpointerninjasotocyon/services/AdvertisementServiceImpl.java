package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementResponseDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

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

    @Override
    public boolean closeAdvertisement(Long advertisementId) {
        Optional<Advertisement> advertisementOptional = advertisementRepository.findById(advertisementId);

        if (advertisementOptional.isPresent()) {
            Advertisement advertisement = advertisementOptional.get();

            User loggedUser = getUserFromSecurityContextHolder();

            if (advertisement.getUser().getUsername().equals(loggedUser.getUsername())) {
                advertisement.setClosed(true);
                advertisementRepository.save(advertisement);
                return true;
            } else {
                return false;
            }
        }
        return false;
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
}
