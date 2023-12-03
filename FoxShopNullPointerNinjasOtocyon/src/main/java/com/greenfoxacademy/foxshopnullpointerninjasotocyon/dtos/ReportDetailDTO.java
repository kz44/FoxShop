package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
}
