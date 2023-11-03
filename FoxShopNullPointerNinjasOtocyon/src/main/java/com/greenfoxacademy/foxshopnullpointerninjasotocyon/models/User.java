package com.greenfoxacademy.foxshopnullpointerninjasotocyon.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @OneToMany()
    private Set<Advertisement> advertisements;

    @OneToMany
    private Set<Message> messages;

    @OneToMany
    private Set<Rate> rates;

    @OneToMany
    private Set<Report> reports;

    public User(String username, String firstName, String lastName, LocalDate dateOfBirth, String email, String password, LocalDateTime registrationDate, LocalDateTime lastLogin, Set<Advertisement> advertisements, Set<Message> messages, Set<Rate> rates, Set<Report> reports) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
        this.advertisements = advertisements;
        this.messages = messages;
        this.rates = rates;
        this.reports = reports;
    }
}
