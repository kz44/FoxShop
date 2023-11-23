package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    Page<Advertisement> findByCategoryIdAndClosedFalse(Long categoryId, Pageable pageable);

    Page<Advertisement> findByPriceLessThanEqualAndClosedFalse(Integer maxPrice, Pageable pageable);

    Page<Advertisement> findByCategoryIdAndPriceLessThanEqualAndClosedFalse(Long categoryId, Integer maxPrice, Pageable pageable);

    Page<Advertisement> findByClosedFalse(Pageable pageable);
}
