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

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findById_Success() throws Exception { // Retourne une session avec succès
        Long id = 1L; // ID de la session

        String expectedName = "Matin"; // Nom attendu de la session
        Long expectedTeacherId = 1L; // ID attendu de l'enseignant
        String expectedDescription = "seance du matin"; // Description attendue de la session

        mockMvc.perform(get("/api/session/{id}", id)) // Effectue une requête GET pour obtenir la session par ID
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$.id", is(id.intValue()))) // Vérifie que l'ID de la session dans la réponse est correct
                .andExpect(jsonPath("$.name", is(expectedName))) // Vérifie que le nom de la session dans la réponse est correct
                .andExpect(jsonPath("$.teacher_id", is(expectedTeacherId.intValue()))) // Vérifie que l'ID de l'enseignant dans la réponse est correct
                .andExpect(jsonPath("$.description", is(expectedDescription))); // Vérifie que la description de la session dans la réponse est correcte
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findById_SessionNotFound() throws Exception { // Retourne une erreur lorsque la session n'est pas trouvée
        Long id = 99L; // ID de la session qui n'existe pas

        mockMvc.perform(get("/api/session/{id}", id)) // Effectue une requête GET pour obtenir la session par ID
                .andExpect(status().isNotFound()); // Vérifie que le statut de la réponse est 404 Not Found
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findAll_Integration() throws Exception { // Retourne toutes les sessions
        mockMvc.perform(get("/api/session")) // Effectue une requête GET pour obtenir toutes les sessions
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$", not(empty()))); // Vérifie que la réponse contient des sessions
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void create() throws Exception { // Crée une nouvelle session avec succès
        SessionDto sessionDto = new SessionDto(); // Crée un DTO de session
        sessionDto.setName("Test Session"); // Définir le nom de la session
        sessionDto.setDate(new Date()); // Définir la date de la session
        sessionDto.setTeacher_id(1L); // Définir l'ID de l'enseignant
        sessionDto.setDescription("This is a test session."); // Définir la description de la session

        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto); // Convertit le DTO en JSON

        mockMvc.perform(post("/api/session") // Effectue une requête POST pour créer une nouvelle session
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête comme JSON
                        .content(sessionDtoJson)) // Ajoute le contenu JSON de la session
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void update() throws Exception { // Met à jour une session existante avec succès
        SessionDto sessionDto = new SessionDto(); // Crée un DTO de session
        sessionDto.setName("Updated Session"); // Définir le nom mis à jour de la session
        sessionDto.setDate(new Date()); // Définir la date de la session
        sessionDto.setTeacher_id(1L); // Définir l'ID de l'enseignant
        sessionDto.setDescription("This is an updated test session."); // Définir la description mise à jour de la session

        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto); // Convertit le DTO en JSON
        Long id = 1L; // ID de la session à mettre à jour

        mockMvc.perform(put("/api/session/{id}", id) // Effectue une requête PUT pour mettre à jour la session
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête comme JSON
                        .content(sessionDtoJson)) // Ajoute le contenu JSON de la session
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void save() throws Exception { // Supprime une session avec succès
        Long id = 1L; // ID de la session à supprimer

        mockMvc.perform(delete("/api/session/{id}", id)) // Effectue une requête DELETE pour supprimer la session
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void participate() throws Exception { // Vérifie qu'un utilisateur peut participer à une session avec succès
        Long id = 2L; // ID de la session
        Long userId = 3L; // ID de l'utilisateur

        mockMvc.perform(post("/api/session/{id}/participate/{userId}", id, userId)) // Effectue une requête POST pour ajouter un utilisateur à une session
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void noLongerParticipate() throws Exception { // Vérifie qu'un utilisateur peut arrêter de participer à une session avec succès
        Long id = 1L; // ID de la session
        Long userId = 3L; // ID de l'utilisateur

        mockMvc.perform(get("/api/session/{id}", id)) // Vérifie que la session existe
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK

        mockMvc.perform(get("/api/user/{id}", userId)) // Vérifie que l'utilisateur existe
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", id, userId) // Effectue une requête DELETE pour retirer un utilisateur d'une session
                        .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête comme JSON
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }
}
