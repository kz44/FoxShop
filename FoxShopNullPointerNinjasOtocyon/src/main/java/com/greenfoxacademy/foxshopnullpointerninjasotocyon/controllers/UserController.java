package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/role")
    public ResponseEntity<?> getUserRoles() {
        String userRole = userService.checkUserRole();
        if (userRole != null) {
            return ResponseEntity.ok(Map.of("role", userRole));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorMessageDTO("Authentication failed."));
        }
    }

    @PostMapping("/ban/{id}")
    public ResponseEntity<?> banUser(@PathVariable(required = false) Long id) {
        return userService.banUserById(id);
    }
}
