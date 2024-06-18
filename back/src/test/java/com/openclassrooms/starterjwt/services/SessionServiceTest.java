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


    @Test
    public void participate_WhenSessionOrUserNotFound_ShouldThrowNotFoundException() { //La session ou l'utilisateur n'est pas trouvé
        // la session et l'utilisateur non idsponible
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Vérification que la méthode lance une NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }


    //La session n'est pas trouvée
    @Test
    public void noLongerParticipate_WhenSessionNotFound_ShouldThrowNotFoundException() { //La session n'est pas trouvée
        // Session non trouvée
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Vérifie que la méthode lance une NotFoundException
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }


    @Test
    public void noLongerParticipate_WhenUserNotParticipates_ShouldThrowBadRequestException() { // L'utilisateur ne participe pas à la session
        // Création d'un utilisateur factice
        User user = new User();
        user.setId(1L);

        // Création d'une session factice avec des utilisateurs
        Session session = mock(Session.class);
        List<User> users = new ArrayList<>();
        when(session.getUsers()).thenReturn(users);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        // Vérifie que la méthode lance une BadRequestException
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }


    @Test
    public void noLongerParticipate_ShouldRemoveUserFromSession() { // L'utilisateur est retiré d'une session
        // Création d'un utilisateur factice
        User user = new User();
        user.setId(1L);

        // Création d'une session factice avec des utilisateurs
        Session session = mock(Session.class);
        List<User> users = new ArrayList<>();
        users.add(user);
        when(session.getUsers()).thenReturn(users);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 1L);

        // Vérifie que la session est mise à jour
        verify(sessionRepository, times(1)).save(session);
        verify(session).setUsers(anyList());
    }



}