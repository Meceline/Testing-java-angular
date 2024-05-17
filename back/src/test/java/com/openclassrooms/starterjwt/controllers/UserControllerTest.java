package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

   /* @Test
    @WithMockUser(username = "yoga@studio.com")
    public void  findByIdTest() throws Exception{
        mockMvc.perform(get("/api/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.email", is("yoga@studio.com")))
                .andExpect(jsonPath("$.lastName", is("Admin")))
                .andExpect(jsonPath("$.firstName", is("John")));
    }*/

 /*   @Test // Indique que c'est une méthode de test
    @WithMockUser // Simule un utilisateur authentifié
    public void testFindById_Success() throws Exception {
        // Given : Les données nécessaires sont déjà dans la base de données de test
        // When
        mockMvc.perform(get("/api/user/{id}", 2L) // Effectue une requête GET à l'URL /api/user/{id} avec l'ID 2
                        .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête à JSON
                // Then
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.id", is(2))) // Vérifie que l'ID de l'utilisateur dans la réponse est 2
                .andExpect(jsonPath("$.email", is("john@email.com"))) // Vérifie que l'email de l'utilisateur dans la réponse est "john@email.com"
                .andExpect(jsonPath("$.lastName", is("DOE"))) // Vérifie que le nom de famille de l'utilisateur dans la réponse est "DOE"
                .andExpect(jsonPath("$.firstName", is("John"))); // Vérifie que le prénom de l'utilisateur dans la réponse est "John"
    }*/

    //DELETE USER
 /*   @Test
    @WithMockUser(username = "yoga@studio.com")
    public void saveTest() throws Exception{
        mockMvc.perform(delete("/api/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/


}
