package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDTO;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<?> nullCheckReport(ReportDTO reportDTO);
}
