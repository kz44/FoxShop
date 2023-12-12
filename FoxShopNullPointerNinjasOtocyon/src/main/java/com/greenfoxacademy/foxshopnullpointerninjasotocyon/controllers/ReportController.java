package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportCreationDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.enums.State;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.ReportService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Retrieves a list of reports specific to the currently authenticated user.
     * <p>
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
     * <p>
     * This endpoint fetches the detailed information of a report based on the provided report ID.
     * If the report ID is missing in the request path, it returns an error message.
     *
     * @param id The ID of the report to retrieve details for.
     * @return A ResponseEntity containing the detailed information of the requested report if found,
     * or an error message if the report ID is missing or the report is not accessible.
     */
    @GetMapping(value = {"/{id}", "/"})
    public ResponseEntity<?> reportDetails(@PathVariable(required = false) Long id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Report id missing in request path."));
        }
        return reportService.reportDetails(id);
    }

    /**
     * Filters database records based on status and paginates the results.
     * <p>
     * This endpoint is accessible only to users with the role of "ADMIN".
     * It allows filtering database records, specifically reports, based on status and pagination.
     *
     * @param status     Optional. The status by which reports are filtered. If null, all reports are fetched.
     * @param pageNumber Optional. The page number to retrieve. Should be a non-negative integer.
     * @return A ResponseEntity containing a paginated list of ReportSummaryDTO objects
     * representing reports filtered by status, along with total pages available.
     * If the user doesn't have the required authorization, returns a bad request response with an error message.
     * If the page number is invalid or missing, returns a bad request response with an error message.
     */
    @GetMapping(value = {"/reports/{pageNumber}", "/reports/"})
    public ResponseEntity<?> filterDatabaseRecords(@RequestParam(required = false) String status,
                                                   @PathVariable(required = false) Integer pageNumber) {
        if (!(
                userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("ADMIN") ||
                        userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("DEVELOPER")
        )) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Access not authorized."));
        }
        if (pageNumber == null) {
            pageNumber = 0;
        }
        if (pageNumber < 0) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Please insert a valid number of pages for the displayed results"));
        }
        return reportService.browseReportsByStatus(pageNumber, status);
    }

    /**
     * Accepts an advertisement report identified by its ID.
     * <p>
     * This endpoint is accessible only to users with the role of "ADMIN".
     * It allows an administrator to accept an advertisement report by changing its state to accepted.
     *
     * @param id The ID of the advertisement report to accept.
     * @return A ResponseEntity indicating the success or failure of the acceptance operation.
     * If the user doesn't have the required authorization, returns a bad request response with an error message.
     * If the report ID is missing in the request path, returns a bad request response with an error message.
     */
    //    URI path with double slash /reports//accept is not recognised as a valid URi by postman, the system returns message 400 Bad request
    @PostMapping(value = {"/reports/{id}/accept", "/reports/accept"})
    public ResponseEntity<?> acceptAdvertisementReport(@PathVariable(required = false) Long id) {
        if (!(
                userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("ADMIN") ||
                        userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("DEVELOPER")
        )) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Access not authorized."));
        }
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Report id missing in request path."));
        }
        return reportService.acceptOrDenyReport(id, State.ACCEPTED);
    }

    /**
     * Denies an advertisement report identified by its ID.
     * <p>
     * This endpoint is accessible only to users with the role of "ADMIN".
     * It allows an administrator to deny an advertisement report by changing its state to denied.
     *
     * @param id The ID of the advertisement report to deny.
     * @return A ResponseEntity indicating the success or failure of the denial operation.
     * If the user doesn't have the required authorization, returns a bad request response with an error message.
     * If the report ID is missing in the request path, returns a bad request response with an error message.
     */
    @PostMapping(value = {"/reports/{id}/deny", "/reports/deny"})
    public ResponseEntity<?> denyAdvertisementReport(@PathVariable(required = false) Long id) {
        if (!(
                userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("ADMIN") ||
                        userService.getUserFromSecurityContextHolder().getRole().getRoleName().equals("DEVELOPER")
        )) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Access not authorized."));
        }
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Report id missing in request path."));
        }
        return reportService.acceptOrDenyReport(id, State.DENIED);
    }


}
