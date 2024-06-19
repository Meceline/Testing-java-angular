package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Intégration
    @Test
    void registerUserSuccess() throws Exception { // Enregistrement d'un utilisateur avec succès
        // Crée une demande d'inscription avec des informations d'utilisateur
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test23@example.com"); // Définir l'email du nouvel utilisateur
        signupRequest.setFirstName("Test"); // Définir le prénom du nouvel utilisateur
        signupRequest.setLastName("User"); // Définir le nom de famille du nouvel utilisateur
        signupRequest.setPassword("password"); // Définir le mot de passe du nouvel utilisateur

        // Effectue une requête POST pour enregistrer l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register") // Effectue une requête POST pour enregistrer l'utilisateur
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête comme JSON
                        .content(objectMapper.writeValueAsString(signupRequest))) // Convertit la demande d'inscription en JSON
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(content().json("{\"message\":\"User registered successfully!\"}")); // Vérifie le contenu de la réponse
    }

    // Intégration
    @Test
    void registerUserFailEmailTaken() throws Exception { // Echec enregistrement d'un utilisateur lorsque l'email est déjà pris
        // Crée une demande d'inscription avec un email déjà existant
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com"); // Définir un email déjà pris
        signupRequest.setFirstName("Test"); // Définir le prénom du nouvel utilisateur
        signupRequest.setLastName("User"); // Définir le nom de famille du nouvel utilisateur
        signupRequest.setPassword("password"); // Définir le mot de passe du nouvel utilisateur

        // Enregistre une fois l'utilisateur dans le dépôt pour simuler l'email déjà pris
        userRepository.save(new User("test@example.com", "User", "Test", passwordEncoder.encode("password"), false)); // Enregistrer un utilisateur existant

        // Effectue une requête POST pour enregistrer l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register") // Effectue une requête POST pour enregistrer l'utilisateur
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête comme JSON
                        .content(objectMapper.writeValueAsString(signupRequest))) // Convertit la demande d'inscription en JSON
                .andExpect(status().isBadRequest()) // Vérifie que le statut de la réponse est 400 Bad Request
                .andExpect(content().json("{\"message\":\"Error: Email is already taken!\"}")); // Vérifie le contenu de la réponse
    }

    // Intégration
    @Test
    void authenticateUserSuccess() throws Exception { // Authentification réussie
        // Crée et enregistre un utilisateur avec email et mot de passe
        String email = "yoga1@studio.com"; // Définir l'email de l'utilisateur
        String password = "test!1234"; // Définir le mot de passe de l'utilisateur
        userRepository.save(new User(email, "Admin", "Admin", passwordEncoder.encode(password), true)); // Enregistrer l'utilisateur

        // Crée une demande de connexion avec les mêmes informations d'utilisateur
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email); // Définir l'email pour la connexion
        loginRequest.setPassword(password); // Définir le mot de passe pour la connexion

        // Effectue une requête POST pour authentifier l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login") // Effectue une requête POST pour authentifier l'utilisateur
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête comme JSON
                        .content(objectMapper.writeValueAsString(loginRequest))) // Convertit la demande de connexion en JSON
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }

    // Intégration
    @Test
    void authenticateUserFailBadCredentials() throws Exception { // Echec de l'authentification avec des informations incorrectes
        // Crée une demande de connexion avec des informations incorrectes
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com"); // Définir un email incorrect
        loginRequest.setPassword("wrongpassword"); // Définir un mot de passe incorrect

        // Effectue une requête POST pour authentifier l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login") // Effectue une requête POST pour authentifier l'utilisateur
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête comme JSON
                        .content(objectMapper.writeValueAsString(loginRequest))) // Convertit la demande de connexion en JSON
                .andExpect(status().isUnauthorized()); // Vérifie que le statut de la réponse est 401 Unauthorized
    }
}
