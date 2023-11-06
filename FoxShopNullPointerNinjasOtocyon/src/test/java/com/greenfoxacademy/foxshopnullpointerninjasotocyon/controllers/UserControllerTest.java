package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private final UserController userController = new UserController();


    @Test
    public void registrationNullCheckResponseIsOK() {
        RegisterDto registerDto = new RegisterDto("JohnSmith", "John", "Smith", "john.smith@test.com", "password", LocalDate.of(1980, 11, 1));
        ResponseEntity<?> response = userController.registrationNullCheck(registerDto);
        assertEquals(ResponseEntity.ok().build(), response);
    }

    @Test
    public void registrationNullCheckResponseIsBadMissingUsername() {
        RegisterDto registerDto = new RegisterDto(null, "John", "Smith", "john.smith@test.com", "password", LocalDate.of(1980, 11, 1));
        ResponseEntity<?> response = userController.registrationNullCheck(registerDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: username.", errorMessageDTO.getError());
    }

    @Test
    public void registrationNullCheckResponseIsBadMissingPasswordAndDateOfBirth() {
        RegisterDto registerDto = new RegisterDto("johnSmith", "John", "Smith", "john.smith@test.com", null, null);
        ResponseEntity<?> response = userController.registrationNullCheck(registerDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: password, date of birth.", errorMessageDTO.getError());
    }

    @Test
    public void registrationNullCheckResponseIsBadMissingAllData() {
        RegisterDto registerDto = new RegisterDto(null, null, null, null, null, null);
        ResponseEntity<?> response = userController.registrationNullCheck(registerDto);
        assertInstanceOf(ErrorMessageDTO.class, response.getBody());
        ErrorMessageDTO errorMessageDTO = (ErrorMessageDTO) response.getBody();
        assertNotNull(errorMessageDTO);
        assertEquals("There are missing some data in your request: username, email, password, date of birth.", errorMessageDTO.getError());
    }


}