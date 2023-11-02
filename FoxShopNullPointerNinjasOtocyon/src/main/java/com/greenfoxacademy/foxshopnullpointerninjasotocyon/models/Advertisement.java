package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    private Location location;

    @ManyToOne
    private User user;

    @ManyToOne
    private DeliveryMethod deliveryMethod;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Condition condition;

    @ManyToMany
    private Set<ImagePaths> imagePaths;
}
