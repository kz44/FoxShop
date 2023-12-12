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

    /**
     * Handles HTTP GET requests to check the previous ratings of a user.
     * This endpoint allows clients to retrieve information about the previous ratings
     * given by a user identified by their username. The method performs validation checks
     * on the provided username and responds with either the user's ratings or appropriate
     * error messages.
     *
     * @param username (optional) The username of the user whose previous ratings are to be checked.
     * @return ResponseEntity representing the HTTP response, including status codes and response body.
     *         - 200 OK: If the user has previous ratings, returns a list of RateDTO objects.
     *         - 400 Bad Request: If the provided username is empty or null.
     *         - 404 Not Found: If no user is found with the given username.
     *         - 200 OK: If the user has no previous ratings, returns a success message.
     */

    @GetMapping(value = {"/{username}", "/"})
    public ResponseEntity<?> checkPreviousRatings(@PathVariable (required = false) String username){
        if(username == null){
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
