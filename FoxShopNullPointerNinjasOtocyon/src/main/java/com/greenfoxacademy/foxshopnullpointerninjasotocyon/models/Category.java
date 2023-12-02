package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "parentCategory")
    private Set<Category> subCategories;

    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "category")
    private Set<Advertisement> advertisements;
}
