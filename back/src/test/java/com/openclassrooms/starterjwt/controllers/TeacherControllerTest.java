package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findById() throws Exception { // Retourne un enseignant avec succès
        mockMvc.perform(get("/api/teacher/{id}", 1L) // Effectue une requête GET pour obtenir l'enseignant par ID
                        .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête comme JSON
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$.id", is(1))) // Vérifie que l'ID de l'enseignant dans la réponse est correct
                .andExpect(jsonPath("$.firstName", is("Margot"))) // Vérifie que le prénom de l'enseignant dans la réponse est correct
                .andExpect(jsonPath("$.lastName", is("DELAHAYE"))); // Vérifie que le nom de famille de l'enseignant dans la réponse est correct
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findAll() throws Exception { // Retourne tous les enseignants avec succès
        mockMvc.perform(get("/api/teacher") // Effectue une requête GET pour obtenir tous les enseignants
                        .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête comme JSON
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$", hasSize(2))) // Vérifie que le nombre d'enseignants dans la réponse est correct
                .andExpect(jsonPath("$[0].id", is(1))) // Vérifie que l'ID du premier enseignant dans la réponse est correct
                .andExpect(jsonPath("$[0].firstName", is("Margot"))) // Vérifie que le prénom du premier enseignant dans la réponse est correct
                .andExpect(jsonPath("$[0].lastName", is("DELAHAYE"))); // Vérifie que le nom de famille du premier enseignant dans la réponse est correct
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById_NotFound() throws Exception { // Retourne une erreur lorsque l'enseignant n'est pas trouvé
        Long teacherId = 999L; // ID de l'enseignant qui n'existe pas
        when(teacherService.findById(teacherId)).thenReturn(null); // Simule le service pour renvoyer null

        mockMvc.perform(get("/api/teacher/{id}", teacherId)) // Effectue une requête GET pour obtenir l'enseignant par ID
                .andExpect(status().isNotFound()); // Vérifie que le statut de la réponse est 404 Not Found
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById_InvalidId() throws Exception { // Retourne une erreur lorsque l'ID de l'enseignant est invalide
        mockMvc.perform(get("/api/teacher/{id}", "abc")) // Effectue une requête GET pour obtenir l'enseignant par ID invalide
                .andExpect(status().isBadRequest()); // Vérifie que le statut de la réponse est 400 Bad Request
    }

}
