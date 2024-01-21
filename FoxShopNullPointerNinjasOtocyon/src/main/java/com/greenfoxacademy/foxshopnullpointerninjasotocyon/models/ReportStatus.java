package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.enums.State;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReportStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    //    enum with string field: pending, accepted, denied
    @Column(unique = true, nullable = false)
    private String state = State.PENDING.getStatusValue();

    @OneToMany(mappedBy = "reportStatus")
    private Set<Report> reports;

    public void setReportStatus(State state) {
        this.state = state.getStatusValue();
    }

    @Override
    public String toString() {
        return "ReportStatus{" +
                "id=" + id +
                ", state='" + state + '\'' +
                '}';
    }
}
