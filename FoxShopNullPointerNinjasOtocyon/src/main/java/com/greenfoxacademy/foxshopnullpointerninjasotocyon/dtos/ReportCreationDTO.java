package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReportCreationDTO {
    private String title;
    private String description;
    private Long receiver;
}
