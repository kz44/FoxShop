package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilteredReportsDto {
    private Page<Report> filteredResultPage;
    private Integer pagesInDatabaseTotal;

}
