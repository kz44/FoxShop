package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RateDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.SuccessMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Rate;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.RateService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@RestController
@AllArgsConstructor
@RequestMapping("/api/ratings")
public class RateController {

    private final RateService rateService;
    private final UserService userService;

    @GetMapping(value = {"/{username}", "/"})
    public ResponseEntity<?> checkPreviousRatings(@PathVariable (required = false) String username){
        if(username == null || username.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("Username cannot be empty. Please provide a valid username."));
        }

        Optional<User> user = userService.findByUsername(username);
        if (user.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDTO("I'm sorry, but we couldn't find this username. Please double-check and try again."));
        }

        List<RateDTO> ratingDTO = rateService.findRates(username);

        if (ratingDTO.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessMessageDTO("There have been no purchases from this user yet."));
        }

        return ResponseEntity.ok(ratingDTO);
    }
}
