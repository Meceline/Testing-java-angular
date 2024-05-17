package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;


 @Test
    @WithMockUser(username = "user@test.com")
    public void findByIdTest() throws Exception {
     SessionDto sessionDto = new SessionDto();
     sessionDto.setId(18L);

        Long id = 18L;

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(18)))
                .andExpect(jsonPath("$.name", is("1")))
                .andExpect(jsonPath("$.teacher_id", is(2)))
                .andExpect(jsonPath("$.description", is("de")));
    }

    /*
    @Test
    @WithMockUser(username = "user@test.com")
    public void findByI_dNotFoundTest() throws Exception{
        Long id = 123456L;
        mockMvc.perform((get("/api/session/{id}", id)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "user@test.com")
    public void findAllTest() throws Exception{
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }


    @Test
    @WithMockUser(username = "user@test.com")
    public void createTest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("new session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("description");

        String sessionDtoJson = new ObjectMapper().writeValueAsString(sessionDto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sessionDtoJson))
                .andExpect(status().isOk());
    }

   */
/* @Test
    @WithMockUser(username = "user@test.com")
    public void updateTest() throws Exception {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName("updated session");
        sessionDto.setDate(new Date());
        sessionDto.setTeacher_id(2L);
        sessionDto.setDescription("new description");

        String sessionsDtoJson = new ObjectMapper().writeValueAsString(sessionDto);

        Long id = 1L;

        mockMvc.perform(post("/api/session/$id", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessionsDtoJson))
                .andExpect(status().isOk());
    }*//*


    //DELETE SESSION
  */
/*  @Test
    @WithMockUser(username = "yoga@studio.com")
    public void saveTest() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/api/session/{id}", id))
                .andExpect(status().isOk());
    }*//*

   */
/* @Test
    @WithMockUser(username = "user@test.com")
    public void participateTest() throws Exception {
        Long sessionId = 1L;
        Long userId = 2L;
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId))
                .andExpect(status().isOk());
    }*//*


    */
/*@Test
    @WithMockUser(username = "user@test.com")
    public void noLongerParticipateTest() throws Exception {
        Long sessionId = 1L;
        Long userId = 2L;

        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId))
                .andExpect(status().isOk());
    }*/


}

