package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<?> nullCheckReport(ReportDTO reportDTO);

    ResponseEntity<?> createNewReport(ReportDTO reportDTO);
}
