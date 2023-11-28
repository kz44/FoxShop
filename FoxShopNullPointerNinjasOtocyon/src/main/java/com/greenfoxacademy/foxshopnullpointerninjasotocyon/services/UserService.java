package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RegisterDto;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.FoxUserDetails;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String name);

    Optional<User> findByLoginDTO(LoginDTO loginDTO);

    boolean checkPassword(User user, String password);

    ResponseEntity<?> nullCheckLogin(LoginDTO loginDTO);

    public boolean doesUsernameAlreadyExist(String username);

    boolean doesEmailAlreadyExist(String email);

    void constructAndSaveUser(RegisterDto registerDto);

    ResponseEntity<?> registrationNullCheck(RegisterDto registerDto);

    void handleSecurityContextAndBlacklistToken();

    User getUserFromSecurityContextHolder();

    String checkUserRole();
}
