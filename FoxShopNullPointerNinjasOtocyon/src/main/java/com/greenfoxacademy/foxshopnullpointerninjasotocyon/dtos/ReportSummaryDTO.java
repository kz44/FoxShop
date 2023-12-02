package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportSummaryDTO {
    private String reportTitle;
    private Long reportID;
    private String reportStatus;
    private String advertisementTitle;
}

/*
Path variable - id
ReportDetailDTO
GET
reportDescription, username, reportStatus, reportTitle, advertisementId, advertisementTitle
 */