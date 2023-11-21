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
     * This method check all required fields in newAdvertisementDto.
     * It returns Response Entity OK if everything is ok,
     * or it returns EntityResponse bad and in the body is new ErrorMessageDto with message
     * which field are missing.
     *
     * @param advertisementDto
     * @return ResponseEntity
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
     * There is created new Advertisement and the user is set on it.
     * User is get from repository find by username which is received from Security Context Holder.
     * At the end of this method another method is calling to validate all ids (of category, location, delivery method, condition)
     * and in case of successful validation save the entity. It returns the response from another validation method.
     *
     * @param advertisementDto
     * @return ResponseEntity
     */

    @Override
    public ResponseEntity<?> createNewAdvertisement(AdvertisementDto advertisementDto) {
        Advertisement advertisement = new Advertisement();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).get();
        advertisement.setUser(user);
        return dataValidationAndSaveAdvertisement(advertisementDto, advertisement);
    }

    /**
     * This method checks if all ids (of category, location, delivery method, condition) are valid.
     * If ids are valid, new entity of Advertisement is saved in database. Response is OK with id in body.
     * If ids are not valid. Response is BAD and in body is ErrorMessage with message which id was invalid.     *
     *
     * @param advertisementDto
     * @return Response Entity
     */

    private ResponseEntity<?> dataValidationAndSaveAdvertisement(AdvertisementDto advertisementDto, Advertisement advertisement) {
        List<String> errors = new ArrayList<>();
        advertisement.setTitle(advertisementDto.getTitle());
        advertisement.setDescription(advertisementDto.getDescription());
        advertisement.setPrice(advertisementDto.getPrice());
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
