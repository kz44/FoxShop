package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {


    ResponseEntity<?> registrationNullCheck(RegisterDto registerDto) {
        List<String> missingProperties = new ArrayList<>();
        if (registerDto.getUsername() == null) {
            missingProperties.add("username");
        }
        if (registerDto.getEmail() == null) {
            missingProperties.add("email");
        }
        if (registerDto.getPassword() == null) {
            missingProperties.add("password");
        }
        if (registerDto.getDateOfBirth() == null) {
            missingProperties.add("date of birth");
        }
        if (!missingProperties.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingProperties)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

}
