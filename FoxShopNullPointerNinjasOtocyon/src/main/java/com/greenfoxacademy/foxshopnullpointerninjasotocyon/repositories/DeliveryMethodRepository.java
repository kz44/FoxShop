package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {
}
