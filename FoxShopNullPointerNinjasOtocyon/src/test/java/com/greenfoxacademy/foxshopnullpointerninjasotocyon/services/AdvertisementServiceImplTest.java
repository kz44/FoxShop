package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementCreationDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AdvertisementServiceImplTest {
    @MockBean
    private AdvertisementRepository advertisementRepository = Mockito.mock(AdvertisementRepository.class);
    @MockBean
    private LocationRepository locationRepository = Mockito.mock(LocationRepository.class);
    @MockBean
    private CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    @MockBean
    private ConditionRepository conditionRepository = Mockito.mock(ConditionRepository.class);
    @MockBean
    private DeliveryMethodRepository deliveryMethodRepository = Mockito.mock(DeliveryMethodRepository.class);
    @MockBean
    private UserService userService = Mockito.mock(UserService.class);
    private final AdvertisementService advertisementService = new AdvertisementServiceImpl(advertisementRepository, locationRepository, categoryRepository, conditionRepository, deliveryMethodRepository, userService);


    @Test
    void nullCheckNewAdvertisementIsOk() {
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("Title", "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementCreationDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void nullCheckNewAdvertisementWithoutTitle() {
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto(null, "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: title.", errorMessageDTO.getMessage());
    }

    @Test
    void nullCheckNewAdvertisementWithoutPriceAndLocationId() {
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("title", "description", null, null, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: price, location id.", errorMessageDTO.getMessage());
    }

    @Test
    void nullCheckNewAdvertisementWithoutAnyData() {
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto(null, null, null, null, null, null, null);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: title, description, price, category id, condition id, location id, delivery method id.", errorMessageDTO.getMessage());
    }

    @Test
    void createNewAdvertisementEverythingOk() {
        User user = new User();
        user.setUsername("testUsername");
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("Title", "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementCreationDto);
        assertInstanceOf(SuccessMessageDTO.class, response.getBody());
        SuccessMessageDTO successMessageDTO = (SuccessMessageDTO) response.getBody();
        assertNotNull(successMessageDTO);
    }

    @Test
    void createNewAdvertisementWrongCategoryId() {
        User user = new User();
        user.setUsername("testUsername");
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("Title", "description", 100, 4L, 5L, 2L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Wrong category id.", errorMessageDTO.getMessage());
    }

    @Test
    void createNewAdvertisementWrongAllIds() {
        User user = new User();
        user.setUsername("testUsername");
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("Title", "description", 100, 5L, 6L, 2L, 4L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Wrong category id. Wrong condition id. Wrong location id. Wrong delivery method id.", errorMessageDTO.getMessage());
    }

    @Test
    void createNewAdvertisementNegativePrice() {
        User user = new User();
        user.setUsername("testUsername");
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("Title", "description", -100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Price must be positive number.", errorMessageDTO.getMessage());
    }

    @Test
    void updateAdvertisementEverythingOk() {
        User user = new User();
        user.setUsername("testUsername");
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setTitle("OldTitle");
        advertisement.setDescription("OldDescription");
        advertisement.setPrice(50);
        advertisement.setUser(user);
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("NewTitle", "NewDescription", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.updateAdvertisement(1L, advertisementCreationDto);
        assertInstanceOf(SuccessMessageDTO.class, response.getBody());
        SuccessMessageDTO successMessageDTO = (SuccessMessageDTO) response.getBody();
        assertNotNull(successMessageDTO);
        assertEquals("Your advertisement with id 1 was successfully updated.", successMessageDTO.getMessage());
    }

    @Test
    void updateAdvertisementWrongIdOfAdvertisement() {
        User user = new User();
        user.setUsername("testUsername");
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setTitle("OldTitle");
        advertisement.setDescription("OldDescription");
        advertisement.setPrice(50);
        advertisement.setUser(user);
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("NewTitle", "NewDescription", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.updateAdvertisement(2L, advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There is no advertisement with this id.", errorMessageDTO.getMessage());
    }

    @Test
    void updateAdvertisementByNonOwner() {
        User user = new User();
        User anotherUser = new User();
        user.setUsername("testUsername");
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setTitle("OldTitle");
        advertisement.setDescription("OldDescription");
        advertisement.setPrice(50);
        advertisement.setUser(anotherUser);
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("NewTitle", "NewDescription", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.updateAdvertisement(1L, advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("It is not possible to change another user's advertisement.", errorMessageDTO.getMessage());
    }

    @Test
    void updateAdvertisementWrongCategoryIdAndWrongLocationId() {
        User user = new User();
        user.setUsername("testUsername");
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setTitle("OldTitle");
        advertisement.setDescription("OldDescription");
        advertisement.setPrice(50);
        advertisement.setUser(user);
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("NewTitle", "NewDescription", 100, 14L, 5L, 11L, 3L);
        ResponseEntity<?> response = advertisementService.updateAdvertisement(1L, advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Wrong category id. Wrong location id.", errorMessageDTO.getMessage());
    }

    @Test
    void updateAdvertisementPriceNegativeNumber() {
        User user = new User();
        user.setUsername("testUsername");
        Advertisement advertisement = new Advertisement();
        advertisement.setId(1L);
        advertisement.setTitle("OldTitle");
        advertisement.setDescription("OldDescription");
        advertisement.setPrice(50);
        advertisement.setUser(user);
        Mockito.when(userService.getUserFromSecurityContextHolder()).thenReturn(user);
        Mockito.when(advertisementRepository.findById(1L)).thenReturn(Optional.of(advertisement));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto("NewTitle", "NewDescription", -100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.updateAdvertisement(1L, advertisementCreationDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Price must be positive number.", errorMessageDTO.getMessage());
    }
}