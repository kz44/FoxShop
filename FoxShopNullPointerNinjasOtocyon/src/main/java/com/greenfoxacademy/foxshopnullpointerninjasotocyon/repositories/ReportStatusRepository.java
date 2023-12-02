package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;

import java.util.Optional;

public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long> {
Optional<ReportStatus> findDistinctByState(String status);

}
