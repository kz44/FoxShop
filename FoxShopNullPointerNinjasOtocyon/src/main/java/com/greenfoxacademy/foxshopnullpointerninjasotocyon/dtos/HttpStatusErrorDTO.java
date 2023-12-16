package com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class HttpStatusErrorDTO {
    String response;

    public HttpStatusErrorDTO(int error) {
        if (error == 404) {
            response = "The page you are looking for does not exist.";
        } else if (error == 403) {
            response = "You are not authorized to access this page.";
        } else if (error == 500) {
            response = "Something went wrong! Our Engineers are on it.";
        } else response = "Something went wrong! However, we could not find the cause of the problem.";
    }
    }
