package com.greenfoxacademy.foxshopnullpointerninjasotocyon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan("com.greenfoxacademy.foxshopnullpointerninjasotocyon.config")
@SpringBootApplication
public class FoxShopNullPointerNinjasOtocyonApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoxShopNullPointerNinjasOtocyonApplication.class, args);
    }
}
