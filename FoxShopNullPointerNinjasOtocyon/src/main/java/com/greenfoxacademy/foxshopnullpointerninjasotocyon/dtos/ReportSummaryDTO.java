package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportSummaryDTO {
    private String reportTitle;
    private Long reportID;
    private String reportStatus;
    private String advertisementTitle;

    public ReportSummaryDTO(Report report) {
        reportTitle = report.getTitle();
        reportID = report.getId();
        reportStatus = report.getReportStatus().getState();
        advertisementTitle = report.getReceiver().getTitle();
    }
}