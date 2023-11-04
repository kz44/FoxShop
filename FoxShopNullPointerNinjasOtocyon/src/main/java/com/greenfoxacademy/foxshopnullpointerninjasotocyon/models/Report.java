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
    private String description;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Advertisement receiver;
}