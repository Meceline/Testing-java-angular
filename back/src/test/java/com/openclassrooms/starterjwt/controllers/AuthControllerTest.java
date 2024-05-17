/*
package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test // Indique que c'est une méthode de test
    public void testLogin_Success() throws Exception {
        // Given : Un utilisateur avec l'email "john@email.com" et le mot de passe "password" existe dans la base de données de test
        // Crée une requête de connexion avec l'email et le mot de passe de l'utilisateur
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@email.com");
        loginRequest.setPassword("password");

        // Convertit la requête de connexion en JSON
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);

        // When
        mockMvc.perform(post("/api/auth/login") // Effectue une requête POST à l'URL /api/auth/login
                        .contentType(MediaType.APPLICATION_JSON) // Définit le type de contenu de la requête à JSON
                        .content(jsonLoginRequest)) // Définit le contenu de la requête à la requête de connexion en JSON
                // Then
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 (OK)
                .andExpect(jsonPath("$.token", is(notNullValue()))); // Vérifie que le token dans la réponse n'est pas null
    }

   */
/*@Test
    public void authenticatUserTest() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(notNullValue())));
    }*//*

    @Test
    public void authenticatUserWithBadEmailTest() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("USER@test.com");
        loginRequest.setPassword("falsePassword");
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andExpect(status().isUnauthorized());
    }

  */
/*  @Test
    public void registerUserTest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@test.com");
        signupRequest.setPassword("azqs");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        String jsonSignupRequest = new ObjectMapper().writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully!")));
    }*//*


    @Test
    public void registerUserBadRequestTest() throws Exception{
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("user@test.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("First");
        signupRequest.setLastName("Last");

        String jsonSignupRequest = new ObjectMapper().writeValueAsString(signupRequest);

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("{\"message\":\"Error: Email is already taken!\"}"));

    }

}*/



package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUserSuccess() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\":\"User registered successfully!\"}"));
    }

    @Test
    void registerUserFailEmailTaken() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setFirstName("Test");
        signupRequest.setLastName("User");
        signupRequest.setPassword("password");

        // Register the user once
        userRepository.save(new User("test@example.com", "User", "Test", passwordEncoder.encode("password"), false));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"message\":\"Error: Email is already taken!\"}"));
    }

    @Test
    void authenticateUserSuccess() throws Exception {
        // Register a user
        String email = "test@example.com";
        String password = "password";
        userRepository.save(new User(email, "User", "Test", passwordEncoder.encode(password), false));

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
        // Additional assertions for the response can be added here
    }

    @Test
    void authenticateUserFailBadCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}

