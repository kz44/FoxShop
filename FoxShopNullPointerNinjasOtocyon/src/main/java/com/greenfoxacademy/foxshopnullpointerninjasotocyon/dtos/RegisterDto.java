package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    /**
     * default LocalDate format: yyyy-mm-dd: "2023-11-03"
     * custom:
     *
     * @DateTimeFormat(pattern = "dd/MM/yyyy")
     */
    private LocalDate dateOfBirth;

    @Override
    public String toString() {
        return "RegisterDto{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='*****'" +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
