package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.empty;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void findByIdTest() throws Exception {
        Long id = 1L;

        mockMvc.perform(get("/api/session/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is("Séance de Yoga matin")))
                .andExpect(jsonPath("$.teacher_id", is(1L)))
                .andExpect(jsonPath("$.description", is("Une séance de yoga revitalisante pour bien commencer la journée.")));
    }

    @Test
    @WithMockUser
    public void findByIdNotFoundTest() throws Exception{
        Long id = 123456L;
        mockMvc.perform((get("/api/session/{id}", id)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findAllTest() throws Exception{
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }


    @Test
    @WithMockUser
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

}
