package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportFilteredDTO {
    //    private List<Report> filteredResultPage;
    private List<ReportSummaryDTO> reports;
    private Integer pagesInDatabaseTotal;

}
