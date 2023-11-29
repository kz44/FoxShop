package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {


    /**
     * Searches for advertisements based on the specified criteria.
     *
     * @param categoryId The ID of the category to filter by. Set to null to ignore this filter.
     * @param maxPrice   The maximum price for filtering. Set to code null to ignore this filter.
     * @param pageable   The pagination information for the result set.
     * @return A list of Advertisement objects that match the specified criteria and are not closed.
     */

    @Query("SELECT ad FROM Advertisement ad WHERE " +
            "(:categoryId IS NULL OR ad.category.id = :categoryId) AND " +
            "(:maxPrice IS NULL OR ad.price <= :maxPrice) AND " +
            "ad.closed = false")
    List<Advertisement> searchAdvertisements(@Param("categoryId") Long categoryId, @Param("maxPrice") Integer maxPrice, Pageable pageable);

}
