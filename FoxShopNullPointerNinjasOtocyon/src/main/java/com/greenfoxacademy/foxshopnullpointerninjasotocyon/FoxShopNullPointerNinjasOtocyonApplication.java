package com.greenfoxacademy.foxshopnullpointerninjasotocyon;


import com.greenfoxacademy.foxshopnullpointerninjasotocyon.models.User;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;


@ConfigurationPropertiesScan("com.greenfoxacademy.foxshopnullpointerninjasotocyon.config")
@SpringBootApplication
public class FoxShopNullPointerNinjasOtocyonApplication implements CommandLineRunner {

    UserRepository userRepository;
    PasswordEncoder encoder;

    public FoxShopNullPointerNinjasOtocyonApplication(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(FoxShopNullPointerNinjasOtocyonApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        User user = new User();
//        user.setUsername("Lenka");
//        user.setEmail("lenka@lenka.com");
//        user.setDateOfBirth(LocalDate.of(1980, 1, 1));
//        user.setRegistrationDate(LocalDateTime.of(2023, 1, 1, 3, 3));
//        user.setPassword(encoder.encode("password"));
//        userRepository.save(user);


    }
}
