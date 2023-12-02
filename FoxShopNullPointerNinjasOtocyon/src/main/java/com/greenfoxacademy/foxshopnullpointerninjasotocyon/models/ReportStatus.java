package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

//    enum with string field: pending, accepted, denied
   @Column(unique = true, nullable = false)
    private String state = "pending";

    @OneToMany(mappedBy = "reportStatus")
    private Set<Report> reports;

}
