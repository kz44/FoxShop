package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

}
