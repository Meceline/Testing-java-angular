package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findById_Success() throws Exception { //Retourne une session avec succès
        Long id = 1L;

        String expectedName = "Matin";
        Long expectedTeacherId = 1L;
        String expectedDescription = "seance du matin";

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.name", is(expectedName)))
                .andExpect(jsonPath("$.teacher_id", is(expectedTeacherId.intValue())))
                .andExpect(jsonPath("$.description", is(expectedDescription)));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findById_SessionNotFound() throws Exception { //Retourne une erreur lorsque la session n'est pas trouvée
        // ID de la session qui n'existe pas
        Long id = 99L;

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findAll_Integration() throws Exception { //Retourne toutes les sessions
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com") //Crée une nouvelle session avec succès
    public void create() throws Exception {
        // Crée un DTO de session avec les informations de test
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("This is a test session.");

        // Convertit le DTO en JSON
        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void update() throws Exception { // Met à jour une session existante avec succès
        // Crée un DTO de session avec les informations mises à jour
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("This is an updated test session.");

        // Convertit le DTO en JSON
        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);
        // ID de la session à mettre à jour
        Long id = 1L;

        // Effectue une requête PUT pour mettre à jour la session et vérifie la réponse
        mockMvc.perform(put("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void save() throws Exception { //Supprime une session avec succès
        // ID de la session à supprimer
        Long id = 1L;

        // Effectue une requête DELETE pour supprimer la session et vérifie la réponse
        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void participate() throws Exception { // Vérifie qu'un utilisateur peut participer à une session avec succès
        // ID de la session et de l'utilisateur
        Long id = 2L;
        Long userId = 3L;

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", id, userId))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void noLongerParticipate() throws Exception { // Vérifie qu'un utilisateur peut arrêter de participer à une session avec succès
        // ID de la session et de l'utilisateur
        Long id = 1L;
        Long userId = 3L;

        // Vérifie que la session existe
        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK

        // Vérifie que l'utilisateur existe
        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK

        // Effectue une requête DELETE pour retirer l'utilisateur de la session et vérifie la réponse
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", id, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK
    }
}

