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

    /**
     * Retrieves a list of reports specific to the currently authenticated user.
     *
     * This endpoint fetches reports associated with the authenticated user.
     * It returns a ResponseEntity containing the retrieved reports as per the user's access rights.
     *
     * @return A ResponseEntity containing a list of reports accessible to the authenticated user.
     */
    @GetMapping("/reports")
    public ResponseEntity<?> reportsByUser() {
        return ResponseEntity.ok(reportService.browseReportsByUser());
    }

    /**
     * Retrieves detailed information about a specific report identified by its ID.
     *
     * This endpoint fetches the detailed information of a report based on the provided report ID.
     * If the report ID is missing in the request path, it returns an error message.
     *
     * @param id The ID of the report to retrieve details for.
     * @return A ResponseEntity containing the detailed information of the requested report if found,
     *         or an error message if the report ID is missing or the report is not accessible.
     */
    @GetMapping(value = {"/{id}", "/"})
    public ResponseEntity<?> reportDetails(@PathVariable(required = false) Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Report id missing in request path."));
        }
        return reportService.reportDetails(id);
    }
}
