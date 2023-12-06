package com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.ReportStatus;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findAllBySender(User sender);
    Page<Report> findAll(Pageable pageable);
    Page<Report> findAllByReportStatus(Pageable pageable, ReportStatus reportStatus);

    Optional<Report> findDistinctById(Long id);
}
