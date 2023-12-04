package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RegisterSuccessDto {
    private String username;
    private String email;
    private String message = "Please, finish the registration by verifying the email address via the link in the email which was just send to your mailbox.";
    public RegisterSuccessDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
