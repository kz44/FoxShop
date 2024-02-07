package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RateDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Rate;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.RateRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService{

    public final RateRepository rateRepository;
    public final UserRepository userRepository;
    @Override
    public List<RateDTO> findRates(String username){
        Optional<User> user = userRepository.findByUsername(username);
        Set<Rate> userRates = user.get().getRates();

        List<RateDTO> ratingDTOs = userRates.stream()
                .map(rate -> new RateDTO(rate.getAdvertisement().getId(), rate.getRating(), rate.getDescription()))
                .collect(Collectors.toList());

        return ratingDTOs;
    }
}
