package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    /**
     * default LocalDate format: yyyy-mm-dd: "2023-11-03"
     * custom:
     *  @DateTimeFormat(pattern = "dd/MM/yyyy")
     */
    private LocalDate dateOfBirth;

}
