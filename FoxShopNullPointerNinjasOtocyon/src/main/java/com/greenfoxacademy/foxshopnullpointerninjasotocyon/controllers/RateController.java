package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RateDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Rate;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
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
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ratings")
public class RateController {

    private final RateService rateService;
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<?> checkPreviousRatings(@PathVariable String username){
        if(username.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorMessageDTO("No given user name."));
        }

        Optional<User> currentUser = userService.findByUsername(username);
        Set<Rate> userRates = currentUser.get().getRates();

        if(currentUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessageDTO("The person you are looking for cannot be found, check that it is written correctly."));
        }

        if(userRates.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("There have been no purchases from this user yet.");
        }

        List<RateDTO> ratingDTOs = userRates.stream()
                .map(rate -> new RateDTO(rate.getAdvertisement().getId(), rate.getRating(), rate.getDescription()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ratingDTOs);
    }
}
