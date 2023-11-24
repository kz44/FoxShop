package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementResponseDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.FoxUserDetails;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
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
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @MockBean
    private ImagePathRepository imagePathRepository = Mockito.mock(ImagePathRepository.class);
    @MockBean
    JwtTokenService jwtTokenService = Mockito.mock(JwtTokenService.class);
    private final AdvertisementService advertisementService = new AdvertisementServiceImpl(
            advertisementRepository, locationRepository, categoryRepository,
            conditionRepository, deliveryMethodRepository, userRepository,
            imagePathRepository, jwtTokenService);


    @Test
    void nullCheckNewAdvertisementIsOk() {
        AdvertisementDto advertisementDto = new AdvertisementDto("Title", "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    void nullCheckNewAdvertisementWithoutTitle() {
        AdvertisementDto advertisementDto = new AdvertisementDto(null, "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: title.", errorMessageDTO.getMessage());
    }

    @Test
    void nullCheckNewAdvertisementWithoutPriceAndLocationId() {
        AdvertisementDto advertisementDto = new AdvertisementDto("title", "description", null, null, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: price, location id.", errorMessageDTO.getMessage());
    }

    @Test
    void nullCheckNewAdvertisementWithoutAnyData() {
        AdvertisementDto advertisementDto = new AdvertisementDto(null, null, null, null, null, null, null);
        ResponseEntity<?> response = advertisementService.nullCheckAdvertisement(advertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: title, description, price, category id, condition id, location id, delivery method id.", errorMessageDTO.getMessage());
    }

    @Test
    void createNewAdvertisementEverythingOk() {
        User user = new User();
        user.setUsername("testUsername");
        FoxUserDetails foxUserDetails = FoxUserDetails.fromUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(foxUserDetails, null, List.of(new SimpleGrantedAuthority("user")));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementDto advertisementDto = new AdvertisementDto("Title", "description", 100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementDto);
        assertInstanceOf(AdvertisementResponseDto.class, response.getBody());
        AdvertisementResponseDto advertisementResponseDto = (AdvertisementResponseDto) response.getBody();
        assertNotNull(advertisementResponseDto);
    }

    @Test
    void createNewAdvertisementWrongCategoryId() {
        User user = new User();
        user.setUsername("testUsername");
        FoxUserDetails foxUserDetails = FoxUserDetails.fromUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(foxUserDetails, null, List.of(new SimpleGrantedAuthority("user")));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementDto advertisementDto = new AdvertisementDto("Title", "description", 100, 4L, 5L, 2L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Wrong category id.", errorMessageDTO.getMessage());
    }

    @Test
    void createNewAdvertisementWrongAllIds() {
        User user = new User();
        user.setUsername("testUsername");
        FoxUserDetails foxUserDetails = FoxUserDetails.fromUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(foxUserDetails, null, List.of(new SimpleGrantedAuthority("user")));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementDto advertisementDto = new AdvertisementDto("Title", "description", 100, 5L, 6L, 2L, 4L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Wrong category id. Wrong condition id. Wrong location id. Wrong delivery method id.", errorMessageDTO.getMessage());
    }

    @Test
    void createNewAdvertisementNegativePrice() {
        User user = new User();
        user.setUsername("testUsername");
        FoxUserDetails foxUserDetails = FoxUserDetails.fromUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(foxUserDetails, null, List.of(new SimpleGrantedAuthority("user")));
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(userRepository.findByUsername("testUsername")).thenReturn(Optional.of(user));
        Mockito.when(categoryRepository.findById(1L)).thenReturn(Optional.of(new Category(1L, "testCategory", null, null, null)));
        Mockito.when(conditionRepository.findById(3L)).thenReturn(Optional.of(new Condition(1L, "testCondition", null)));
        Mockito.when(locationRepository.findById(4L)).thenReturn(Optional.of(new Location(1L, "testLocation", null)));
        Mockito.when(deliveryMethodRepository.findById(5L)).thenReturn(Optional.of(new DeliveryMethod(1L, "testDeliveryMethod", null)));
        AdvertisementDto advertisementDto = new AdvertisementDto("Title", "description", -100, 4L, 5L, 1L, 3L);
        ResponseEntity<?> response = advertisementService.createNewAdvertisement(advertisementDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are some errors in your request: Price must be positive number.", errorMessageDTO.getMessage());
    }
}