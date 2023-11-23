package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private ReportService reportService;
    @PostMapping("/create")
    public ResponseEntity<?> reportAdvertisement(@RequestBody(required = false) ReportDTO reportDTO) {
        if (reportDTO == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data to report the advertisement."));
        }
        ResponseEntity<?> responseNullCheck = reportService.nullCheckReport(reportDTO);
        if (!responseNullCheck.getStatusCode().is2xxSuccessful()) {
            return responseNullCheck;
        }
        return reportService.createNewReport(reportDTO);
    }
}
