package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.TokenResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginDTO loginDTO) {
        if (loginDTO == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data for login."));
        }
        ResponseEntity<?> response = nullCheckLogin(loginDTO);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        //TODO need to do authentication and generate token
        return ResponseEntity.ok().body(new TokenResponseDTO()); //TODO add token
    }

    public ResponseEntity<?> nullCheckLogin(LoginDTO loginDTO) {
        List<String> errors = new ArrayList<>();
        if (loginDTO.getEmail() == null && loginDTO.getUsername() == null) {
            errors.add("Please, provide username or email for your authentication.");
        }
        if (loginDTO.getEmail() != null && loginDTO.getUsername() != null) {
            errors.add("Please, choose only one identification data - email or username.  ");
        }
        if (loginDTO.getPassword() == null) {
            errors.add("There is missing password in your login request.");
        }
        if (!errors.isEmpty()) {
            String message = String.join(" ", errors);
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

}
