package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FilteredReportsDto {
    private List<Report> filteredResultPage;
    private Integer pagesInDatabaseTotal;

}
