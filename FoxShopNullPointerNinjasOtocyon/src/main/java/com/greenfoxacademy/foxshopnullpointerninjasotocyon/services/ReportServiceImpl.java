package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Advertisement;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.AdvertisementRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.ReportRepository;
import lombok.AllArgsConstructor;
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

    /**
     * Checks for null values in the provided AdvertisementDto and returns an appropriate ResponseEntity.
     * <p>
     * This method examines the essential fields of the ReportDTO, ensuring none of them are null.
     * If any null values are found, it returns a ResponseEntity with a Bad Request status and an error message
     * indicating the missing data. Otherwise, it returns a ResponseEntity with an OK status.
     *
     * @param reportDTO The ReportDTO to be checked for null values.
     * @return ResponseEntity<?> A ResponseEntity indicating the status of the null check.
     * - If null values are found, it returns a Bad Request status with an error message.
     * - If no null values are found, it returns an OK status.
     */

    @Override
    public ResponseEntity<?> nullCheckReport(ReportDTO reportDTO) {
        List<String> missingData = new ArrayList<>();
        if (reportDTO.getDescription() == null) {
            missingData.add("description");
        }
        if (reportDTO.getReceiver() == null) {
            missingData.add("receiver");
        }
        if (!missingData.isEmpty()) {
            String message = "There are missing some data in your request: ".concat(String.join(", ", missingData)).concat(".");
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        return ResponseEntity.ok().build();
    }

    /**
     * Creates a new report using the provided ReportDTO and associates it with the current authenticated user.
     * <p>
     * This method initializes a new Report entity, sets the current user as its owner, and delegates to the
     * dataValidationAndSaveReport method for data validation and saving.
     *
     * @param reportDTO The ReportDTO containing the information for the new advertisement.
     * @return ResponseEntity<?> A ResponseEntity containing either the saved Report ID or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    @Override
    public ResponseEntity<?> createNewReport(ReportDTO reportDTO) {
        Report report = new Report();
        User user = userService.getUserFromSecurityContextHolder();
        report.setSender(user);
        return dataValidationAndSaveReport(reportDTO, report);
    }

    /**
     * Performs data validation for the provided ReportDTO and updates the given Report entity.
     * If validation passes, the updated Report is saved to the repository.
     *
     * @param reportDTO The ReportDTO containing the updated information for the advertisement.
     * @param report    The Report entity to be updated.
     * @return ResponseEntity<?> A ResponseEntity containing either the saved Report ID or an error message,
     * wrapped in the appropriate HTTP status code.
     */

    private ResponseEntity<?> dataValidationAndSaveReport(ReportDTO reportDTO, Report report) {
        List<String> errors = new ArrayList<>();
        report.setDescription(reportDTO.getDescription());
        Optional<Advertisement> advertisement = advertisementRepository.findById(reportDTO.getReceiver());
        advertisement.ifPresentOrElse(report::setReceiver, () -> errors.add("Wrong advertisement id."));
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
            return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        reportRepository.save(report);
        return ResponseEntity.ok().body("Report sent successfully.");
    }
}
