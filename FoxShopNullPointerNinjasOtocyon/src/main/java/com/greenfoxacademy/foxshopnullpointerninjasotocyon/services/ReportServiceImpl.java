package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.*;
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
        report.setReportStatus(reportStatusRepository.findDistinctByState("pending").get());
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
//        status field can only be updated by admin + report status can be updated, but not set at creation - DTO field should not be required
        Optional<ReportStatus> reportStatus = reportStatusRepository.findDistinctByState(reportCreationDTO.getReportStatus());
        User user = userService.getUserFromSecurityContextHolder();
        reportStatus.ifPresent(r -> {
            if (user.getRole().getRoleName().equals("ADMIN")) {
                report.setReportStatus(reportStatus.get());
            } else {
                errors.add("Report status can be changed only by admin.");
            }
        });
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        reportRepository.save(report);
        return ResponseEntity.ok().body(new SuccessMessageDTO("Report sent successfully."));
    }

    @Override
    public List<ReportSummaryDTO> reportsToDTOs() {
        //the api/reports endpoint using this method is being accessible only to authenticated users:
        List<Report> reports = reportRepository.findAllBySender(userService.getUserFromSecurityContextHolder());
        List<ReportSummaryDTO> reportSummaries = new ArrayList<>();
        for (Report r : reports) {
            reportSummaries.add(new ReportSummaryDTO(r.getTitle(), r.getId(), r.getReportStatus().getState(), r.getReceiver().getTitle()));
        }
        return reportSummaries;
    }

    @Override
    public ResponseEntity<?> reportOverview(Long reportID) {
        Optional<Report> report = reportRepository.findById(reportID);
        if (report.isEmpty()) {
            return ResponseEntity.badRequest().body("The advertisement id is not located in database");
        }
        return ResponseEntity.ok(new ReportDetailDTO(
                report.get().getTitle(), report.get().getDescription(),
                report.get().getSender().getUsername(), report.get().getReportStatus().getState(),
                report.get().getReceiver().getId(), report.get().getReceiver().getTitle()
        ));
    }
}
