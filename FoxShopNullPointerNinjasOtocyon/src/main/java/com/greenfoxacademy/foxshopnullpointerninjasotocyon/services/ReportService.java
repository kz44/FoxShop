package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import org.springframework.http.ResponseEntity;

public interface ReportService {
    ResponseEntity<?> nullCheckReport(ReportCreationDTO reportCreationDTO);

    ResponseEntity<?> createNewReport(ReportCreationDTO reportCreationDTO);
}
