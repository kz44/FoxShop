package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private ReportService reportService;

    @PostMapping("/create")
    public ResponseEntity<?> reportAdvertisement(@RequestBody(required = false) ReportCreationDTO reportCreationDTO) {
        if (reportCreationDTO == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("There is missing the body of request with all data to report the advertisement."));
        }
        ResponseEntity<?> responseNullCheck = reportService.nullCheckReport(reportCreationDTO);
        if (!responseNullCheck.getStatusCode().is2xxSuccessful()) {
            return responseNullCheck;
        }
        return reportService.createNewReport(reportCreationDTO);
    }

    @GetMapping("/reports")
    public ResponseEntity<?> reportsByUser() {
        return ResponseEntity.ok(reportService.browseReportsByUser());
    }

    @GetMapping(value = {"/{id}", "/"})
    public ResponseEntity<?> reportDetails(@PathVariable(required = false) Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Report id missing in request path."));
        }
        return reportService.reportDetails(id);
    }
}
