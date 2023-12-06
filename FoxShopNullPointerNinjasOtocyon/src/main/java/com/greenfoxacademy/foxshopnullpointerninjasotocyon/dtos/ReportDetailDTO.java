package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ReportDetailDTO {
    private String reportTitle;
    private String description;
    private String creator;
    private String reportStatus;
    private Long advertisementId;
    private String advertisementTitle;


    /* GET request with:
Path variable - id
ReportDetailDTO

reportDescription, username, reportStatus, reportTitle, advertisementId, advertisementTitle
 */
    public ReportDetailDTO(Report r) {
        reportTitle = r.getTitle();
        description = r.getDescription();
        creator = r.getSender().getUsername();
        reportStatus = r.getReportStatus().getState();
        advertisementId = r.getReceiver().getId();
        advertisementTitle = r.getReceiver().getTitle();
    }
}
