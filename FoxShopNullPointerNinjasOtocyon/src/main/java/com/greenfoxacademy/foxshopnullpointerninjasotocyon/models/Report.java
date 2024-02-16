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

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    private boolean statusChange = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement receiver;

    @ManyToOne
    @JoinColumn(name = "reportStatus_id", nullable = false)
    private ReportStatus reportStatus;

    public boolean getStatusChange() {
        return statusChange;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", statusChange=" + statusChange +
                ", sender=" + sender.getUsername() +
                ", receiver=" + receiver.getId() +
                ", reportStatus=" + reportStatus +
                '}';
    }
}