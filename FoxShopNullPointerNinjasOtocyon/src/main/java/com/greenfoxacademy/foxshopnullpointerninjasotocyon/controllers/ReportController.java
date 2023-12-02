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
//        List l = new ArrayList<>();
//        ReportSummaryDTO rsd = new ReportSummaryDTO("title",1L,"pending","advertisement");
//l.add(rsd);
//        return l;
//        return ResponseEntity.ok().body(rsd);
                return reportService.reportsToDTOs();
    }
}
