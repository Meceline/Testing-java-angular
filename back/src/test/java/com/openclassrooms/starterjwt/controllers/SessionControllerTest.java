package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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
    public void testFindById_Success() throws Exception {
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
    public void testFindById_SessionNotFound() throws Exception {
        Long id = 99L;
        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isNotFound());
    }
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindAll_Integration() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testCreate() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Test Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("This is a test session.");

        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testUpdate() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("Updated Session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("This is an updated test session.");

        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);
        Long id = 1L;
        mockMvc.perform(put("/api/session/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDelete() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void participateTest() throws Exception {
        Long id = 2L;
        Long userId = 3L;
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", id, userId))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void shouldNoLongerParticipateInSession() throws Exception {
        Long id = 1L;
        Long userId = 3L;

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/user/{id}", userId))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", id, userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

