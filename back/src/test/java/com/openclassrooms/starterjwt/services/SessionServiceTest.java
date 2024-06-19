package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    // Unitaire
    @Test
    public void participate_WhenSessionOrUserNotFound_ShouldThrowNotFoundException() { //La session ou l'utilisateur n'est pas trouvé
        // la session et l'utilisateur non idsponible
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty()); // Simule la non-disponibilité d'une session dans le repository.
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty()); // Simule la non-disponibilité d'un utilisateur dans le repository.

        // Vérification que la méthode lance une NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 1L); // Appelle la méthode participate et vérifie qu'une NotFoundException est lancée.
        });
    }

    // Unitaire
    @Test
    public void noLongerParticipate_WhenSessionNotFound_ShouldThrowNotFoundException() { //La session n'est pas trouvée
        // Session non trouvée
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty()); // Simule la non-disponibilité d'une session dans le repository.

        // Vérifie que la méthode lance une NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L); // Appelle la méthode noLongerParticipate et vérifie qu'une NotFoundException est lancée.
        });
    }

    // Unitaire
    @Test
    public void noLongerParticipate_WhenUserNotParticipates_ShouldThrowBadRequestException() { // L'utilisateur ne participe pas à la session
        // Création d'un utilisateur factice
        User user = new User();
        user.setId(1L);

        // Création d'une session factice avec des utilisateurs
        Session session = mock(Session.class); // Crée un mock de la classe Session.
        List<User> users = new ArrayList<>();
        when(session.getUsers()).thenReturn(users); // Simule la récupération de la liste des utilisateurs de la session.
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session)); // Simule la disponibilité d'une session dans le repository.

        // Vérifie que la méthode lance une BadRequestException
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L); // Appelle la méthode noLongerParticipate et vérifie qu'une BadRequestException est lancée.
        });
    }

    // Unitaire
    @Test
    public void noLongerParticipate_ShouldRemoveUserFromSession() { // L'utilisateur est retiré d'une session
        // Création d'un utilisateur factice
        User user = new User();
        user.setId(1L);

        // Création d'une session factice avec des utilisateurs
        Session session = mock(Session.class); // Crée un mock de la classe Session.
        List<User> users = new ArrayList<>();
        users.add(user); // Ajoute l'utilisateur à la liste des utilisateurs de la session.
        when(session.getUsers()).thenReturn(users); // Simule la récupération de la liste des utilisateurs de la session.
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session)); // Simule la disponibilité d'une session dans le repository.

        sessionService.noLongerParticipate(1L, 1L); // Appelle la méthode noLongerParticipate pour retirer l'utilisateur de la session.

        // Vérifie que la session est mise à jour
        verify(sessionRepository, times(1)).save(session); // Vérifie que la méthode save du sessionRepository est appelée une fois.
        verify(session).setUsers(anyList()); // Vérifie que la méthode setUsers de la session est appelée.
    }

}
