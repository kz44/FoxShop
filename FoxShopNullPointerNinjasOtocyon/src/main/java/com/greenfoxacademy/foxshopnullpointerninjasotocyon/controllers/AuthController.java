package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.FoxUserDetails;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (!user.isVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO("This user account is not active yet, because email verification is missing. Please check your mailbox and verify your email."));
        }
        if (!userService.checkPassword(user, loginDTO.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO("Wrong credentials."));
        }
        FoxUserDetails authDetails = new FoxUserDetails(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole().getRoleName());
        String token = jwtTokenService.generateToken(authDetails);
        return ResponseEntity.ok().body(new TokenResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody(required = false) RegisterDto registerDto) {
       throw new RuntimeException("");
//        if (registerDto == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("Registration data missing"));
//        }
//
//        ResponseEntity<?> nullChecksResult = userService.registrationNullCheck(registerDto);
//        if (nullChecksResult.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
//            return nullChecksResult;
//        }
//
//        if (userService.doesUsernameAlreadyExist(registerDto.getUsername())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("This username is already being used."));
//        }
//        if (userService.doesEmailAlreadyExist(registerDto.getEmail())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("This email is already being used."));
//        }
//
//        userService.constructAndSaveUser(registerDto);
//        return ResponseEntity.ok(new RegisterSuccessDto(registerDto.getUsername(), registerDto.getEmail()));
    }

    //  using custom logout endpoint instead of the pre-defined one from the security filter chain:
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        userService.handleSecurityContextAndBlacklistToken();
        return ResponseEntity.ok("Logout successful");
    }

    /**
     * Handles a GET request to verify a user's email.
     * <p>
     * This method is mapped to the endpoint "/verify" using the GET method. It expects two query parameters,
     * userId and token, to identify the user and the verification token. The user's email verification is
     * triggered by invoking the corresponding service method.
     *
     * @param userId The unique identifier of the user whose email is to be verified.
     * @param token  The verification token associated with the user's email verification process.
     * @return A ResponseEntity representing the result of the email verification process.
     * The response status and body may vary depending on the success or failure of the verification.
     */
    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserEmail(@RequestParam(required = false) Long userId, @RequestParam(required = false) String token) {
        if (userId == null || token == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Invalid verification link."));
        }
        return userService.verifyUserEmail(userId, token);
    }
}
