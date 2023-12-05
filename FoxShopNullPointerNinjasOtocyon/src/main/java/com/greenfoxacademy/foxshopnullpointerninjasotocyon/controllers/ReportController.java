package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportSummaryDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    public List<ReportSummaryDTO> reportsByUser() {
        return reportService.reportsToDTOs();
    }

    @GetMapping(value = {"/{id}", "/"})
    public ResponseEntity<?> reportDetails(@PathVariable(required = false) Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Advertisement id missing in request path.");
        }
        return reportService.reportOverview(id);
    }
    @GetMapping(value={"/reports/{pageNumber}", "/reports/"})
    public ResponseEntity<?> filterDatabaseRecords(@RequestParam(required = false) String status,
                                                   @PathVariable(required = false) Integer pageNumber) {
        if (pageNumber == null){
            return ResponseEntity.badRequest().body("Please insert the number of pages for the displayed results");
        }
        return reportService.reportFiltering(pageNumber, status);
    }

    @GetMapping("/reportTableSize")
    public Integer reportTableSize (){
        return reportService.reportTableSizeSQL();
    }

}
