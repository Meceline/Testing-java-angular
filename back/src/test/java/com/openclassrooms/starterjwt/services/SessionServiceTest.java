package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
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
    public void participate_WhenSessionOrUserNotFound_ThrowsNotFoundException() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    @Test
    public void participate_WhenUserAlreadyParticipates_ThrowsBadRequestException() {
        User user = new User();
        user.setId(1L);
        Session session = mock(Session.class);
        List<User> users = new ArrayList<>();
        users.add(user);
        when(session.getUsers()).thenReturn(users);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(1L, 1L);
        });
    }

    @Test
    public void noLongerParticipate_WhenSessionNotFound_ThrowsNotFoundException() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(1L, 1L);
        });
    }


}