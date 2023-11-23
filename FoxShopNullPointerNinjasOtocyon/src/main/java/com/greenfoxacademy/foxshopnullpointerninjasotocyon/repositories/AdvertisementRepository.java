package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    /**
     *  Return a page of advertisements filtered by category ID and not closed.
     *
     *  @param categoryId The ID of the category to filter advertisements.
     *  @param pageable   Pageable object contains the page number, size.
     *  @return A page of advertisements filtered on the specified category and not closed.
     */
    Page<Advertisement> findByCategoryIdAndClosedFalse(Long categoryId, Pageable pageable);

    /**
     *  Return a page of advertisements filtered by maxPrice and not closed.
     *
     * @param maxPrice  The maximum price to filter advertisements.
     * @param pageable  Pageable object contains the page number, size.
     * @return A page of advertisements filtered on the specified maxPrice and not closed.
     */
    Page<Advertisement> findByPriceLessThanEqualAndClosedFalse(Integer maxPrice, Pageable pageable);

    /**
     *  Return a page of advertisements filtered by categoryId, maxPrice and not closed.
     *
     * @param categoryId  The ID of the category to filter advertisements.
     * @param maxPrice  The maximum price to filter advertisements.
     * @param pageable  Pageable object contains the page number, size.
     * @return A page of advertisements filtered on the specified categoryId, maxPrice and not closed.
     */
    Page<Advertisement> findByCategoryIdAndPriceLessThanEqualAndClosedFalse(Long categoryId, Integer maxPrice, Pageable pageable);

    /**
     *  Return a page of advertisements filtered by not closed.
     *
     * @param pageable  Pageable object contains the page number, size.
     * @return A page of advertisements which not closed.
     */
    Page<Advertisement> findByClosedFalse(Pageable pageable);
}
