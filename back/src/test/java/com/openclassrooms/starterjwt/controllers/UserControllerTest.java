package com.openclassrooms.starterjwt.controllers;

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

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById() throws Exception { // Retourne un utilisateur avec succès
        // ID de l'utilisateur à tester
        Long userId = 3L;

        mockMvc.perform(get("/api/user/{id}", userId)) // Effectue une requête GET pour obtenir l'utilisateur par ID
                .andExpect(status().isOk()) // Vérifie que le statut de la réponse est 200 OK
                .andExpect(jsonPath("$.id", is(3))) // Vérifie que l'ID de l'utilisateur dans la réponse est correct
                .andExpect(jsonPath("$.email", is("jane@email.com"))) // Vérifie que l'email de l'utilisateur dans la réponse est correct
                .andExpect(jsonPath("$.lastName", is("DOE"))) // Vérifie que le nom de famille de l'utilisateur dans la réponse est correct
                .andExpect(jsonPath("$.firstName", is("Jane"))); // Vérifie que le prénom de l'utilisateur dans la réponse est correct
    }

    // Intégration
    @Test
    @WithMockUser(username = "user")
    public void testFindById_NotFound() throws Exception { // Retourne une erreur lorsque l'utilisateur n'est pas trouvé
        // Arrange
        Long userId = 999L;
        when(userService.findById(userId)).thenReturn(null); // Simule le service pour renvoyer null

        // Act & Assert
        mockMvc.perform(get("/" + userId)) // Effectue une requête GET pour obtenir l'utilisateur par ID
                .andExpect(status().isNotFound()); // Vérifie que le statut de la réponse est 404 Not Found
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testFindById_InvalidId() throws Exception { // Retourne 400 lorsque l'ID est invalide
        mockMvc.perform(get("/api/user/invalidId")) // Effectue une requête GET pour obtenir l'utilisateur par ID invalide
                .andExpect(status().isBadRequest()); // Vérifie que le statut de la réponse est 400 Bad Request
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testSave() throws Exception { // Supprime un utilisateur avec succès
        // ID de l'utilisateur à supprimer
        Long userId = 1L;

        mockMvc.perform(delete("/api/user/{id}", userId) // Effectue une requête DELETE pour supprimer l'utilisateur par ID
                        .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête comme JSON
                .andExpect(status().isOk()); // Vérifie que le statut de la réponse est 200 OK
    }

    // Intégration
    @Test
    @WithMockUser(username = "yoga@studio.com")
    public void testDeleteUserBadRequest() throws Exception { // Retourne une erreur BadRequest lorsque l'ID est invalide
        mockMvc.perform(delete("/api/user/invalidId") // Effectue une requête DELETE pour supprimer l'utilisateur par ID invalide
                        .contentType(MediaType.APPLICATION_JSON)) // Définit le type de contenu de la requête comme JSON
                .andExpect(status().isBadRequest()); // Vérifie que le statut de la réponse est 400 Bad Request
    }

    // Intégration
    @Test
    @WithMockUser(username = "wrong@test.com") // Exécute le test avec un utilisateur mocké dont le nom d'utilisateur est "wrong@test.com"
    public void testDeleteById_Unauthorized() throws Exception { // Retourne une erreur Unauthorized lorsque l'utilisateur n'est pas autorisé
        // Initialise un utilisateur
        User user = new User();
        user.setId(4L);
        user.setEmail("test@test.com");
        // Configure le mock pour retourner cet utilisateur lorsque la méthode findById est appelée
        when(userService.findById(4L)).thenReturn(user);

        mockMvc.perform(delete("/api/user/4")) // Effectue une requête DELETE pour supprimer l'utilisateur par ID
                .andExpect(status().isUnauthorized()); // Vérifie que le statut de la réponse est 401 Unauthorized
    }
}
