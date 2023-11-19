package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class AdvertisementServiceImplTest {
    @MockBean
    private AdvertisementRepository advertisementRepository;
    @MockBean
    private LocationRepository locationRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private ConditionRepository conditionRepository;
    @MockBean
    private DeliveryMethodRepository deliveryMethodRepository;
    @MockBean
    private UserRepository userRepository;

    private final AdvertisementService advertisementService = new AdvertisementServiceImpl(advertisementRepository, locationRepository, categoryRepository, conditionRepository, deliveryMethodRepository, userRepository);


    @Test
    void nullCheckNewAdvertisementIsOk() {
        NewAdvertisementDto newAdvertisementDto = new NewAdvertisementDto("Title", "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckNewAdvertisement(newAdvertisementDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void nullCheckNewAdvertisementWithoutTitle() {
        NewAdvertisementDto newAdvertisementDto = new NewAdvertisementDto(null, "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckNewAdvertisement(newAdvertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: title.", errorMessageDTO.getError());
    }

    @Test
    void nullCheckNewAdvertisementWithoutPriceAndLocationId() {
        NewAdvertisementDto newAdvertisementDto = new NewAdvertisementDto("title", "description", null, null, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckNewAdvertisement(newAdvertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: price, location id.", errorMessageDTO.getError());
    }

    @Test
    void nullCheckNewAdvertisementWithoutAnyData() {
        NewAdvertisementDto newAdvertisementDto = new NewAdvertisementDto(null, null, null, null, null, null, null);
        ResponseEntity<?> response = advertisementService.nullCheckNewAdvertisement(newAdvertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: title, description, price, category id, condition id, location id, delivery method id.", errorMessageDTO.getError());
    }

    @Test
    void createNewAdvertisement() {
    }
}