package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.NewAdvertisementResponseDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

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
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

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
    @WithMockUser(username = "testUsername", authorities = {"USER"})
    void createNewAdvertisementEverythingOk() {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findByUsername(any()))
                .thenReturn(Optional.of(new User(1L, "testUsername", "testFirstName", "testLastName",
                        LocalDate.of(1980, 1, 1),
                        "test@email.com", "password",
                        LocalDateTime.of(2022, 1, 1, 12, 13, 11),
                        null, null, null, null, null, null)));

        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));

        NewAdvertisementDto newAdvertisementDto = new NewAdvertisementDto("Title", "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(newAdvertisementDto);
        assertInstanceOf(NewAdvertisementResponseDto.class, response.getBody());
        NewAdvertisementResponseDto newAdvertisementResponseDto = (NewAdvertisementResponseDto) response.getBody();
        assertNotNull(newAdvertisementResponseDto);
    }
}