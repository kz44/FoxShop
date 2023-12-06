package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDetailDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportSummaryDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportSummaryDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.enums.State;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ReportService {
    ResponseEntity<?> nullCheckReport(ReportCreationDTO reportCreationDTO);
    ResponseEntity<?> createNewReport(ReportCreationDTO reportCreationDTO);
    //    List<ReportSummaryDTO> reportsToDTOs ();
    ResponseEntity<?> reportDetails(Long reportID);
    List<ReportSummaryDTO> browseReportsByUser();
}

