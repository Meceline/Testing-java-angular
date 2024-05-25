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

    @Test
    void registerUserSuccess() throws Exception { //Enregistrement d'un utilisateur avec succès
        // Crée une demande d'inscription avec des informations d'utilisateur
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test23@example.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        // Effectue une requête POST pour enregistrer l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())  // Vérifie que le statut de la réponse est 200 OK
                .andExpect(content().json("{\"message\":\"User registered successfully!\"}"));  // Vérifie le contenu de la réponse
    }


    @Test
    void registerUserFailEmailTaken() throws Exception { // Echec enregistrement d'un utilisateur lorsque l'email est déjà pris
        // Crée une demande d'inscription avec un email déjà existant
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        // Enregistre une fois l'utilisateur dans le dépôt pour simuler l'email déjà pris
        userRepository.save(new User("test@example.com", "User", "Test", passwordEncoder.encode("password"), false));

        // Effectue une requête POST pour enregistrer l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())  // Vérifie que le statut de la réponse est 400 Bad Request
                .andExpect(content().json("{\"message\":\"Error: Email is already taken!\"}"));  // Vérifie le contenu de la réponse
    }


    @Test
    void authenticateUserSuccess() throws Exception { // Authentification réussie
        // Crée et enregistre un utilisateur avec email et mot de passe
        String email = "yoga1@studio.com";
        String password = "test!1234";
        userRepository.save(new User(email, "Admin", "Admin", passwordEncoder.encode(password), true));

        // Crée une demande de connexion avec les mêmes informations d'utilisateur
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        // Effectue une requête POST pour authentifier l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());  // Vérifie que le statut de la réponse est 200 OK
    }


    @Test
    void authenticateUserFailBadCredentials() throws Exception { // Echec de l'authentification avec des informations incorrectes
        // Crée une demande de connexion avec des informations incorrectes
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrongpassword");

        // Effectue une requête POST pour authentifier l'utilisateur et vérifie la réponse
        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());  // Vérifie que le statut de la réponse est 401 Unauthorized
    }
}

