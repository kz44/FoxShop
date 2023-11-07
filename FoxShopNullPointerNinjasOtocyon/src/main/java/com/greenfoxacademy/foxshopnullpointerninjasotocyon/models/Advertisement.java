package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "delivery_method_id")
    private DeliveryMethod deliveryMethod;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    @ManyToOne
    @JoinColumn(name = "condition_id")
    private Condition condition;

    @Column(nullable = false)
    @ManyToMany
    @JoinTable(
            name = "advertisement_image",
            joinColumns = @JoinColumn(name = "advertisement_id"),
            inverseJoinColumns = @JoinColumn(name = "image_paths_id"))
    private Set<ImagePaths> imagePaths;

    @Column(nullable = false)
    @OneToMany(mappedBy = "receiver")
    private Set<Report> reports;

    @Column(nullable = false)
    @OneToOne
    private Set<Rate> rating;
}
