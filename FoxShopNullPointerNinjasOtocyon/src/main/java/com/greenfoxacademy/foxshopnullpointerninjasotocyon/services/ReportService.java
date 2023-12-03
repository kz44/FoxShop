package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDetailDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportSummaryDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportService {
    ResponseEntity<?> nullCheckReport(ReportCreationDTO reportCreationDTO);

    ResponseEntity<?> createNewReport(ReportCreationDTO reportCreationDTO);
    List<ReportSummaryDTO> reportsToDTOs ();
    ResponseEntity<?> reportOverview(Long reportID);

    ResponseEntity<?> reportFiltering (Integer numberPages, String status);
}
