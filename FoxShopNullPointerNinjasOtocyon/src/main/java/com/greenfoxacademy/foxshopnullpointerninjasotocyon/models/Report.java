package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement receiver;

    @ManyToOne
    @JoinColumn(name = "report_status_id", nullable = false)
    private ReportStatus reportStatus;
}