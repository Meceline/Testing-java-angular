package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.Test;
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
    @Autowired // Injecte une instance de MockMvc
    public MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void authenticatUserTest() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        //converti en Json
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON) // Définir le type de contenu
                        .content(jsonLoginRequest)) // Définir le corps de la requête JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(notNullValue())));
    }
    @Test
    public void authenticatUserWithBadEmailTest() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("falseEmail@test.com");
        loginRequest.setPassword("falsePassword");
        //converti en Json
        String jsonLoginRequest = new ObjectMapper().writeValueAsString(loginRequest);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonLoginRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void registerUserTest() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("john33@test.com");
        signupRequest.setPassword("azqs");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");

        String jsonSignupRequest = new ObjectMapper().writeValueAsString(signupRequest);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSignupRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User registered successfully!")));
    }

    @Test
    public void registerUserBadRequestTest() throws Exception{
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
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

}