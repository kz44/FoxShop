package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public ResponseEntity<?> nullCheckImageDto(PostImageDTO postImageDTO) {
        List<String> missingData = new ArrayList<>();
        if (postImageDTO.getImageBase64Encoded() == null) {
            missingData.add("image");
        }
        if (postImageDTO.getAdvertisementId() == null) {
            missingData.add("advertisement id");
        }

        if (!missingData.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingData)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> addImageBase64(String decodedImage, Long id) {
        Optional<Advertisement> advertisement = advertisementRepository.findById(id);
        if (!advertisement.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));}
        int imagesCount = advertisement.get().getImagePaths().size();
        byte[] decodedImageBytes = Base64.getDecoder().decode(decodedImage); //decode String back to binary content:
        String imageFileName = String.valueOf(id) + imagesCount+".jpg";
        String pathForSaving = "src/main/resources/assets/advertisementImages" + File.pathSeparator + imageFileName; //create output file:
        File imageFileObject = new File(pathForSaving);
        try (FileOutputStream stream = new FileOutputStream(imageFileObject)) {//write decodedImageBytes to outputFile:
            stream.write(decodedImageBytes);
        } catch (FileNotFoundException e) {
            System.out.println("File could not be constructed under the path specified.");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));
        } catch (IOException e) {
            System.out.println("Writing bytes into file failed.");
            ResponseEntity.badRequest().body(new ErrorMessageDTO("Posting image not successful."));
        }
        ImagePath image = new ImagePath(imgUrl, advertisementId);
        advertisement.get().getImagePaths().add(image);
        advertisementRepository.save(advertisement);
        imageRepository.save(image);
        return ResponseEntity.ok(new ImgSavedDTO(pathForSaving));
    }




}
