package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


   @Test
   @WithMockUser(username = "yoga@studio.com")
   public void testFindById() throws Exception { // Retourne un utilisateur avec succès
       // ID de l'utilisateur à tester
       Long userId = 3L;

       mockMvc.perform(get("/api/user/{id}", userId))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(3)))
               .andExpect(jsonPath("$.email", is("jane@email.com")))
               .andExpect(jsonPath("$.lastName", is("DOE")))
               .andExpect(jsonPath("$.firstName", is("Jane")));
   }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById_InvalidId() throws Exception { // Retourne 400 lorsque l'ID est invalide
        mockMvc.perform(get("/api/user/invalidId"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testSave() throws Exception { // Supprime un utilisateur avec succès
        // ID de l'utilisateur à supprimer
        Long userId = 1L;

        mockMvc.perform(delete("/api/user/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDeleteUserBadRequest() throws Exception { // Retourne une erreur BadRequest lorsque l'ID est invalide
        mockMvc.perform(delete("/api/user/invalidId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "wrong@test.com") // Exécute le test avec un utilisateur mocké dont le nom d'utilisateur est "wrong@test.com"
    public void testDeleteById_Unauthorized() throws Exception {
        // Initialise un utilisateur
        User user = new User();
        user.setId(4L);
        user.setEmail("test@test.com");
        // Configure le mock pour retourner cet utilisateur lorsque la méthode findById est appelée
        when(userService.findById(4L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/4"))
                .andExpect(status().isUnauthorized());
    }

}
