package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportSummaryDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.enums.State;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.ReportService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/report")
public class ReportController {

    private ReportService reportService;
    private final UserService userService;

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
            return ResponseEntity.badRequest().body("Report id missing in request path.");
        }
        return reportService.reportDetails(id);
    }

    @GetMapping(value = {"/reports/{pageNumber}", "/reports/"})
    public ResponseEntity<?> filterDatabaseRecords(@RequestParam(required = false) String status,
                                                   @PathVariable(required = false) Integer pageNumber) {
        if (!userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("ADMIN")) {
            return ResponseEntity.badRequest().body("Access not authorized.");
        }
        if (pageNumber == null || pageNumber < 0) {
            return ResponseEntity.badRequest().body("Please insert a valid number of pages for the displayed results");
        }
        return reportService.browseReportsByStatus(pageNumber, status);
    }
//    URI path with double slash /reports//accept is not recognised as a valid URi by postman, the system returns message 400 Bad request
    @PostMapping(value = {"/reports/{id}/accept", "/reports/accept"})
    public ResponseEntity<?> acceptAdvertisementReport(@PathVariable(required = false) Long id) {
        if (!userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("ADMIN")) {
            return ResponseEntity.badRequest().body("Access not authorized.");
        }
        if (id == null) {
            return ResponseEntity.badRequest().body("Report id missing in request path.");
        }
        return reportService.acceptOrDenyReport(id, State.ACCEPTED);
    }

    @PostMapping(value = {"/reports/{id}/deny", "/reports/deny"})
    public ResponseEntity<?> denyAdvertisementReport(@PathVariable(required = false) Long id) {
        if (!userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("ADMIN")) {
            return ResponseEntity.badRequest().body("Access not authorized.");
        }
        if (id == null) {
            return ResponseEntity.badRequest().body("Report id missing in request path.");
        }
        return reportService.acceptOrDenyReport(id, State.DENIED);
    }


}
