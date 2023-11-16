package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.aot.generate.GenerationContext;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImagePath {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToMany(mappedBy = "imagePaths")
    private Set<Advertisement> advertisements;

    public ImagePath(String url){
        this.url = url;
    }
}
