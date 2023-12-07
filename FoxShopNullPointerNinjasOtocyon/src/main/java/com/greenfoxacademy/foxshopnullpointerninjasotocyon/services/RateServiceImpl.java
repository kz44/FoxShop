package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Rate;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.RateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService{

    public final RateRepository rateRepository;
    @Override
    public List<Rate> getUserRatings(String username) {
        return rateRepository.findBySenderUsername(username);
    }
}
