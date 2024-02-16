package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class RegisterSuccessDto {
    private String username;
    private String email;
    private String message = "Please, finish the registration by verifying the email address via the link in the email which was just send to your mailbox. The verification link will be valid just for 24 hours.";

    public RegisterSuccessDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
