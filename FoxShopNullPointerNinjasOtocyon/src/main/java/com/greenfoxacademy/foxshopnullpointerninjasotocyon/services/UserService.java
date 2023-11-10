package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;


import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.LoginDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserService {

    Optional<User> findByUsername(String name);

    Optional<User> findByLoginDTO(LoginDTO loginDTO);

    boolean checkPassword(User user, String password);
}
