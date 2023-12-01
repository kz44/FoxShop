package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.mail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailDto {

    private MailUserDto from;
    private MailUserDto to;
    private String subject;
    private String text;


    public MailDto(String username, String email, String link) {
        from = new MailUserDto("foxshop@foxhop.com", "FoxShop");
        this.to = new MailUserDto(email, username);
        subject = "Email verification from FoxShop";
        this.text = String.format("Thank you for your registration! Please verify your email via this link: %s", link);
    }
}
