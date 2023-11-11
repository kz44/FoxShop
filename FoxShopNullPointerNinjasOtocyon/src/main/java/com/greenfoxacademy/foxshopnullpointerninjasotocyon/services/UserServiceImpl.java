package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.BlacklistedJWTToken;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.TokenBlacklistRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.DeleteExpiredToken;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;
    private final DeleteExpiredToken deleteExpiredToken;

    @Override
    public Optional<User> findByUsername(String name) {
        return userRepository.findByUsername(name);
    }

    @Override
    public Optional<User> findByLoginDTO(LoginDTO loginDTO) {
        if (loginDTO.getUsername() != null) {
            return userRepository.findByUsername(loginDTO.getUsername());
        }
        return userRepository.findByEmail(loginDTO.getEmail());
    }

    @Override
    public boolean checkPassword(User user, String password) {
        if (passwordEncoder.matches(password, user.getPassword())) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
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

    public ResponseEntity<?> registrationNullCheck(RegisterDto registerDto) {
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

    public boolean doesUsernameAlreadyExist(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean doesEmailAlreadyExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public void constructAndSaveUser(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        if (registerDto.getFirstName() != null) {
            user.setFirstName(registerDto.getFirstName());
        }
        if (registerDto.getLastName() != null) {
            user.setLastName(registerDto.getLastName());
        }
        user.setDateOfBirth(registerDto.getDateOfBirth());
        user.setRegistrationDate(LocalDateTime.now());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        userRepository.save(user);
    }

    /**
     * The below method is integrated in our custom logout endpoint.
     * After custom logic (blacklisting token),
     * it invokes its own Spring Security’s SecurityContextLogoutHandler,
     * which clears the authentication details from the security context
     * and invalidates the http session
     */
    public void handleSecurityContextAndBlacklistToken() {
        String jwtToken = jwtTokenService.resolveToken(httpServletRequest);
        if (jwtToken != null) {
            BlacklistedJWTToken blacklistedJWTToken = new BlacklistedJWTToken(jwtToken);
            tokenBlacklistRepository.save(blacklistedJWTToken);
            deleteExpiredToken.deleteAfterExpiration(jwtToken);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(httpServletRequest, httpServletResponse, authentication);
    }


}
