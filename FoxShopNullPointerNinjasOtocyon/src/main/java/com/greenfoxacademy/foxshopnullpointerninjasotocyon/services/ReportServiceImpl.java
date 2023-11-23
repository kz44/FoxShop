package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportResponseDTO;
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

    @Override
    public ResponseEntity<?> nullCheckReport(ReportDTO reportDTO) {
        List<String> missingData = new ArrayList<>();
        if (reportDTO.getDescription() == null) {
            missingData.add("description");
        }
        if (reportDTO.getSender() == null) {
            missingData.add("sender");
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

    @Override
    public ResponseEntity<?> createNewReport(ReportDTO reportDTO) {
        Report report = new Report();
        User user = userService.getUserFromSecurityContextHolder();
        report.setSender(user);
        return dataValidationAndSaveReport(reportDTO, report);
    }

    private ResponseEntity<?> dataValidationAndSaveReport(ReportDTO reportDTO, Report report) {
        List<String> errors = new ArrayList<>();
        report.setDescription(reportDTO.getDescription());
        Optional<Advertisement> advertisement = advertisementRepository.findById(reportDTO.getReceiver().getId());
        advertisement.ifPresentOrElse(report::setReceiver, () -> errors.add("Wrong advertisement id."));
        if (!errors.isEmpty()) {
            String message = "There are some errors in your request: ".concat(String.join(" ", errors));
               return ResponseEntity.badRequest().body(new ErrorMessageDTO(message));
        }
        reportRepository.save(report);
        return ResponseEntity.ok().body(new ReportResponseDTO(report.getId()));
    }
}
