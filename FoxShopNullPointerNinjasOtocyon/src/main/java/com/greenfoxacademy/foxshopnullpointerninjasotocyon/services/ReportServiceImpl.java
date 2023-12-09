package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.enums.State;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.ReportStatus;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.AdvertisementRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.ReportRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.ReportStatusRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;
    private UserService userService;
    private AdvertisementRepository advertisementRepository;
    private UserRepository userRepository;
    private ReportStatusRepository reportStatusRepository;

    /**
     * Checks for null values in the provided ReportCreationDTO and returns an appropriate ResponseEntity.
     * <p>
     * This method examines the essential fields of the ReportCreationDTO, ensuring none of them are null.
     * If any null values are found, it returns a ResponseEntity with a Bad Request status and an error message
     * indicating the missing data. Otherwise, it returns a ResponseEntity with an OK status.
     *
     * @param reportCreationDTO The ReportCreationDTO to be checked for null values.
     * @return ResponseEntity<?> A ResponseEntity indicating the status of the null check.
     * - If null values are found, it returns a Bad Request status with an error message.
     * - If no null values are found, it returns an OK status.
     */

    @Override
    public ResponseEntity<?> nullCheckReport(ReportCreationDTO reportCreationDTO) {
        List<String> missingData = new ArrayList<>();
        if (reportCreationDTO.getTitle() == null) {
            missingData.add("title");
        }
        if (reportCreationDTO.getDescription() == null) {
            missingData.add("description");
        }
        if (reportCreationDTO.getReceiver() == null) {
            missingData.add("receiver");
        }
        //report status: default is "pending", so status field does not have to be defined
        if (!missingData.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingData)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new report using the provided ReportCreationDTO and associates it with the current authenticated user.
     * <p>
     * This method initializes a new Report entity, sets the current user as its owner, and delegates to the
     * dataValidationAndSaveReport method for data validation and saving.
     *
     * @param reportCreationDTO The ReportCreationDTO containing the information for the new report.
     * @return ResponseEntity<?> A ResponseEntity containing either a success message or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    @Override
    public ResponseEntity<?> createNewReport(ReportCreationDTO reportCreationDTO) {
        Report report = new Report();
        User user = userService.getUserFromSecurityContextHolder();
        report.setSender(user);
        report.setReportStatus(reportStatusRepository.findDistinctByState(State.PENDING.getStatusValue()).get());
        return dataValidationAndSaveReport(reportCreationDTO, report);
    }

    /**
     * Performs data validation for the provided ReportCreationDTO and updates the given Report entity.
     * If validation passes, the updated Report is saved to the repository.
     *
     * @param reportCreationDTO The ReportCreationDTO containing the updated information for the report.
     * @param report            The Report entity to be updated.
     * @return ResponseEntity<?> A ResponseEntity containing either a success message or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    private ResponseEntity<?> dataValidationAndSaveReport(ReportCreationDTO reportCreationDTO, Report report) {
        List<String> errors = new ArrayList<>();
        report.setTitle(reportCreationDTO.getTitle());
        report.setDescription(reportCreationDTO.getDescription());
        Optional<Advertisement> advertisement = advertisementRepository.findById(reportCreationDTO.getReceiver());
        advertisement.ifPresentOrElse(report::setReceiver, () -> errors.add("Wrong advertisement id."));
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        reportRepository.save(report);
        return ResponseEntity.ok().body(new SuccessMessageDTO("Report sent successfully."));
    }

    /**
     * Retrieves a summary list of reports specific to the currently authenticated user.
     *
     * This method fetches reports from the repository associated with the authenticated user.
     * The API endpoint using this method is restricted to authenticated users only.
     *
     * @return A List of ReportSummaryDTO objects representing summaries of reports accessible to the authenticated user.
     */
    @Override
    public List<ReportSummaryDTO> browseReportsByUser() {
        //the api/reports endpoint using this method is being accessible only to authenticated users:
        List<Report> reports = reportRepository.findAllBySender(userService.getUserFromSecurityContextHolder());
        return reports.stream().map(ReportSummaryDTO::new).toList();
    }

    /**
     * Retrieves detailed information about a specific report identified by its ID.
     *
     * This method fetches the report details based on the provided report ID and the current user's authentication.
     * If the user is not an ADMIN and is not the creator of the report, access to the report details is denied.
     *
     * @param reportID The ID of the report to retrieve details for.
     * @return A ResponseEntity containing the detailed information of the requested report if found,
     *         or an error message if the report is not found in the database or access is restricted.
     */
    @Override
    public ResponseEntity<?> reportDetails(Long reportID) {
        User user = userService.getUserFromSecurityContextHolder();
        Optional<Report> report = reportRepository.findById(reportID);
        if (!user.getRole().getRoleName().equals("ADMIN")) {
            if (report.isPresent() && !report.get().getSender().equals(user)) {
                return ResponseEntity.badRequest().body(new ErrorMessageDTO("The report details can be displayed only to its creator."));
            }
        }
        if (report.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("The report id is not located in database"));
        }
        return ResponseEntity.ok(new ReportDetailDTO(report.get()));
    }

    /**
     * Retrieves reports filtered by status and paginates the results.
     *
     * This method fetches reports from the repository based on the provided status.
     * If the status is null, all reports are fetched and paginated. Otherwise, reports are filtered by the given status.
     * Pagination is applied to control the number of records per page.
     *
     * @param pageNumber The page number to retrieve.
     * @param status     The status by which reports are filtered. If null, all reports are fetched.
     * @return A ResponseEntity containing a paginated list of ReportSummaryDTO objects
     *         representing reports filtered by status, along with total pages available.
     *         If the status is invalid or not found, returns a bad request response with an error message.
     */
    @Override
    public ResponseEntity<?> browseReportsByStatus(Integer pageNumber, String status) {
        Integer recordsPerPage = 3;
        if (status == null) {
            Page<Report> filteredPage = reportRepository.findAll(PageRequest.of(pageNumber, recordsPerPage, Sort.by("reportStatus")));
            List<Report> filteredEntityList = filteredPage.getContent();
            Integer pagesTotal = filteredPage.getTotalPages();
            List<ReportSummaryDTO> filteredDTOs = filteredEntityList.stream().map(ReportSummaryDTO::new).toList();
            return ResponseEntity.ok(new ReportFilteredDTO(filteredDTOs, pagesTotal));
        }

        Optional<ReportStatus> reportStatusOptional = reportStatusRepository.findDistinctByState(status);
        if (reportStatusOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Invalid report status inserted."));
        }

        Page<Report> filteredPage = reportRepository.findAllByReportStatus(PageRequest.of(pageNumber, recordsPerPage, Sort.by("reportStatus")), reportStatusOptional.get());
        List<Report> filteredEntityList = filteredPage.getContent();
        List<ReportSummaryDTO> filteredDTOs = filteredEntityList.stream().map(ReportSummaryDTO::new).toList();
        Integer pagesTotal = filteredPage.getTotalPages();
        return ResponseEntity.ok(new ReportFilteredDTO(filteredDTOs, pagesTotal));
    }

    /**
     * Accepts or denies a report by changing its state.
     *
     * This method modifies the status of a report identified by its ID based on the provided state.
     * The status change is allowed unless the report's status change has already been granted.
     *
     * @param reportId The ID of the report to accept or deny.
     * @param state    The State object representing the desired status change (acceptance or denial).
     * @return A ResponseEntity indicating the success or failure of the status change.
     *         If the report ID is invalid, returns a bad request response with an error message.
     *         If the status change is disallowed due to a previously granted approval, returns a bad request response.
     */
    @Override
    public ResponseEntity<?> acceptOrDenyReport(Long reportId, State state) {
        //no null/validity check on status, as inserted by controller endpoint logic, not manually
        Optional<Report> reportOptional = reportRepository.findById(reportId);
        if (reportOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Invalid report id inserted."));
        }
        if (reportOptional.get().getStatusChange()) {
            return ResponseEntity.badRequest().body(new ErrorMessageDTO("Approval decisions cannot be modified once granted."));
        }
        Optional<ReportStatus> reportStatusOptional = reportStatusRepository.findDistinctByState(state.getStatusValue());
        reportOptional.get().setReportStatus(reportStatusOptional.get());
        reportOptional.get().setStatusChange(true);
        reportRepository.save(reportOptional.get());
        return ResponseEntity.ok(new SuccessMessageDTO("Report successfully " + state.getStatusValue() + "."));
    }

}
