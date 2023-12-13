package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.RateDTO;

import java.util.List;

public interface RateService {

    List<RateDTO> findRates(String username);
}
