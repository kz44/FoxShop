package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.FoxUserDetails;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) LoginDTO loginDTO) {
        if (loginDTO == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data for login."));
        }
        ResponseEntity<?> response = userService.nullCheckLogin(loginDTO);
        if (!response.getStatusCode().is2xxSuccessful()) {
            return response;
        }
        Optional<User> userOpt = userService.findByLoginDTO(loginDTO);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO("User not found."));
        }
        User user = userOpt.get();
        if (!userService.checkPassword(user, loginDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO("Wrong credentials."));
        }
        FoxUserDetails authDetails = new FoxUserDetails(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail());
        String token = jwtTokenService.generateToken(authDetails);
        return ResponseEntity.ok().body(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody(required = false) RegisterDto registerDto) {
        if (registerDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("Registration data missing"));
        }

        ResponseEntity<?> nullChecksResult = userService.registrationNullCheck(registerDto);
        if (nullChecksResult.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
            return nullChecksResult;
        }

        if (userService.doesUsernameAlreadyExist(registerDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("This username is already being used."));
        }
        if (userService.doesEmailAlreadyExist(registerDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("This email is already being used."));
        }

        userService.constructAndSaveUser(registerDto);
        return ResponseEntity.ok(new RegisterSuccessDto(registerDto.getUsername(), registerDto.getPassword()));
    }

}
