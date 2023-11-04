package com.greenfoxacademy.foxshopnullpointerninjasotocyon.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private Set<Category> subCategories;
    private Category parentCategory;
    @OneToMany(mappedBy = "advertisement")
    private Set<Advertisement> advertisements;
}
