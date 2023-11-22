package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.ImagePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImagePathRepository extends JpaRepository<ImagePath, Long> {
}
