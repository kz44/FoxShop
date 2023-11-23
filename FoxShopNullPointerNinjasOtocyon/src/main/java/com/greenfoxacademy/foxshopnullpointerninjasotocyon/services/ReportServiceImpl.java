package com.greenfoxacademy.foxshopnullpointerninjasotocyon.services;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ErrorMessageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.ReportDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private ReportRepository reportRepository;

    @Override
    public ResponseEntity<?> nullCheckReport(ReportDTO reportDTO) {
        List<String> missingData = new ArrayList<>();
        if (reportDTO.getId() == null) {
            missingData.add("id");
        }
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
}
