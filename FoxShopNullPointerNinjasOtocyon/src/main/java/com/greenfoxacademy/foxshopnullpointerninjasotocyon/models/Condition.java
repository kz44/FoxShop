package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Condition_Fox")
@Builder
public class Condition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "condition")
    private Set<Advertisement> advertisements;

    @Override
    public String toString() {
        return "Condition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
