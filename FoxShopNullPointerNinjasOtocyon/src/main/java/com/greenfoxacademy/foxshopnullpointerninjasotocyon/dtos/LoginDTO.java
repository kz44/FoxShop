package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDTO {

    private String username;
    private String email;
    private String password;
}
