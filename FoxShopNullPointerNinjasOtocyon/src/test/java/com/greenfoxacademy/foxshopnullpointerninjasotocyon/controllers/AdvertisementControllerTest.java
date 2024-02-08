package com.greenfoxacademy.foxshopnullpointerninjasotocyon.controllers;

import com.greenfoxacademy.foxshopnullpointerninjasotocyon.dtos.AdvertisementWithImageDTO;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.repositories.TokenBlacklistRepository;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.JwtTokenService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.security.SecurityConfig;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.AdvertisementService;
import com.greenfoxacademy.foxshopnullpointerninjasotocyon.services.UserService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(AdvertisementController.class)
class AdvertisementControllerTest {

    @MockBean
    private AdvertisementService advertisementService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenService jwtTokenService;
    @MockBean
    private TokenBlacklistRepository tokenBlacklistRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAdvertisementByIdOK() throws Exception {
        AdvertisementWithImageDTO advertisementWithImageDTO = new AdvertisementWithImageDTO();
        advertisementWithImageDTO.setTitle("testTitle");
        when(advertisementService.getAdvertisementById(1L)).thenAnswer(a -> ResponseEntity.ok().body(advertisementWithImageDTO));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/advertisement/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("testTitle"));
    }

    @Test
    void getAdvertisementByIdNullIdFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/advertisement/"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Please provide the ID of the advertisement."));
    }

    @Test
    void getImageOK() throws Exception {
        final String imageUrl = "src/test/test/static/assets/advertisementImages/testUsername/1/1.png";
        FileUtils.touch(new File(imageUrl));
        mockMvc.perform(MockMvcRequestBuilders.get("/api/advertisement/getImage")
                        .param("path", "src/test/test/static/assets/advertisementImages/testUsername/1/1.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG));
        Files.delete(Path.of("src/test/test/static/assets/advertisementImages/testUsername/1/1.png"));
        FileUtils.deleteDirectory(new File("src/test/test/"));
    }

    @Test
    void getImageWithoutPathFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/advertisement/getImage"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("The path of the image is missing."));
    }

    @Test
    void getImageWrongPathFailed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/advertisement/getImage")
                        .param("path", "wrongPath"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("There is no picture on provided path."));
    }
}