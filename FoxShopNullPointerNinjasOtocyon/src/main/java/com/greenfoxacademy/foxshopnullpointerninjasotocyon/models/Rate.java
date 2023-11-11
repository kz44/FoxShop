package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User sender;

    @OneToOne
    private Advertisement advertisement;

    public void setRating(int rating) {
        if (rating == 1 || rating == 0 || rating == -1) {
            this.rating = rating;
        } else {
            throw new IllegalArgumentException("Rating must be 1, 0, or -1.");
        }
    }
}
