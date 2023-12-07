package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Rate;

import java.util.List;

public interface RateService {
    List<Rate> getUserRatings(String username);
}
