package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;


import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String name);

    Optional<User> findByLoginDTO(LoginDTO loginDTO);

    boolean checkPassword(User user, String password);

    ResponseEntity<?> nullCheckLogin(LoginDTO loginDTO);

    ResponseEntity<?> registrationNullCheck(RegisterDto registerDto);
}
