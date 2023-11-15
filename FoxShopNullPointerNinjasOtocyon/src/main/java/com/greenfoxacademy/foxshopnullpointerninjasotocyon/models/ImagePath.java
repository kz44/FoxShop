package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.aot.generate.GenerationContext;

import java.util.Set;

@Entity
@Getter
public class ImagePath {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToMany(mappedBy = "imagePaths")
    private Set<Advertisement> advertisements;
}
