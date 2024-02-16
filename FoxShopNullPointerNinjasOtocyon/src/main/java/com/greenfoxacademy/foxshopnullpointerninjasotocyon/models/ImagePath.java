package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImagePath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String url;

    @ManyToOne
    @JoinColumn(name = "advertisement_id", nullable = false)
    private Advertisement advertisement;

    public ImagePath(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ImagePath{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", advertisement=" + advertisement.getId() +
                '}';
    }
}
