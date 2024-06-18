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

    /*@Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;
*/
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findById() throws Exception { // Retourne un enseignant avec succès
        mockMvc.perform(get("/api/teacher/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Margot")))
                .andExpect(jsonPath("$.lastName", is("DELAHAYE")));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void findAll() throws Exception { // Retourne tous les enseignants avec succès
        mockMvc.perform(get("/api/teacher")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Margot")))
                .andExpect(jsonPath("$[0].lastName", is("DELAHAYE")));
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById_NotFound() throws Exception {
        // Arrange
        Long teacherId = 999L;
        when(teacherService.findById(teacherId)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/teacher/{id}", teacherId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById_InvalidId() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }

}
