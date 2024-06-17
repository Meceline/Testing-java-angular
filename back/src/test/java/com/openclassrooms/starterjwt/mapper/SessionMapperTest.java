package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SessionMapperTest {
    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Test
    public void toEntity() { //Converti un Dto en session
        // Crée un DTO utilisateur
        SessionDto sessionDto1 = new SessionDto();
        sessionDto1.setId(1L);
        sessionDto1.setName("Session 1");

        SessionDto sessionDto2 = new SessionDto();
        sessionDto2.setId(2L);
        sessionDto2.setName("Session 2");

        List<SessionDto> dtoList = Arrays.asList(sessionDto1, sessionDto2);

        // Convertit la liste de Dto en Entity
        List<Session> result = sessionMapper.toEntity(dtoList);

        // Vérifie que la conversion a réussi
        assertNotNull(result);
        assertEquals(2, result.size());

        Session session1 = result.get(0);
        assertEquals(sessionDto1.getId(), session1.getId());
        assertEquals(sessionDto1.getName(), session1.getName());

        Session session2 = result.get(1);
        assertEquals(sessionDto2.getId(), session2.getId());
        assertEquals(sessionDto2.getName(), session2.getName());
    }

    @Test
    public void toEntity_withNullInput_returnsNull() {
        // Convertit null en Entity
        List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);

        // Vérifie que la conversion retourne null
        assertNull(result);
    }
    @Test
    public void toDto_withNullInput_returnsNull() {
        // Convertit null en DTO
        List<SessionDto> result = sessionMapper.toDto((List<Session>) null);

        // Vérifie que la conversion retourne null
        assertNull(result);
    }
    @Test
    public void toEntityWithUsers() { //converti un Dto en session avec user
        // Crée un SessionDto avec une liste d'ID d'utilisateurs
        SessionDto sessionDto = new SessionDto();
        sessionDto.setUsers(Arrays.asList(1L, 2L));

        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session result = sessionMapper.toEntity(sessionDto);

        // Vérifie que la conversion a réussi et que les utilisateurs sont corrects
        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
        assertEquals(user1, result.getUsers().get(0));
        assertEquals(user2, result.getUsers().get(1));
    }

    @Test
    public void toDtoWithTeacher() { // converti un dto en session avec teacher
        // Crée une Session avec un enseignant
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Session session = new Session();
        session.setTeacher(teacher);

        SessionDto result = sessionMapper.toDto(session);

        // Vérifie que la conversion a réussi et que l'ID de l'enseignant est correct
        assertNotNull(result);
        assertEquals(teacher.getId(), result.getTeacher_id());
    }

    @Test
    public void toDtoWithUsers() { //converti une session en Dto avec des user
        // Crée une session avec une liste d'utilisateurs
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        Session session = new Session();
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto result = sessionMapper.toDto(session);

        // Vérifie que la conversion a réussi et que les IDs des utilisateurs sont corrects
        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
        assertEquals(Long.valueOf(1L), result.getUsers().get(0));
        assertEquals(Long.valueOf(2L), result.getUsers().get(1));
    }

    @Test
    public void toEntityWithTeacher() { //converti une session en dto avec teacher
        // Crée un SessionDto avec un ID d'enseignant
        SessionDto sessionDto = new SessionDto();
        sessionDto.setTeacher_id(1L);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(teacherService.findById(1L)).thenReturn(teacher);

        Session result = sessionMapper.toEntity(sessionDto);

        // Vérifie que la conversion a réussi et que l'enseignant est correct
        assertNotNull(result);
        assertEquals(teacher, result.getTeacher());
    }
    @Test
    public void testToDtoWithNullTeacher() {
        // Crée une instance de Session sans enseignant
        Session session = new Session();
        session.setTeacher(null);

        SessionDto result = sessionMapper.toDto(session);

        // Vérifie que le résultat n'est pas null
        assertNotNull(result);
        // Vérifie que l'ID de l'enseignant dans le DTO est null
        assertNull(result.getTeacher_id());
    }

    @Test
    public void testToDtoWithTeacherNullId() {
        // Crée une instance de Teacher avec un ID null
        Teacher teacher = new Teacher();
        teacher.setId(null);
        Session session = new Session();
        session.setTeacher(teacher);

        // Convertit la session en DTO
        SessionDto result = sessionMapper.toDto(session);

        assertNotNull(result);
        // Vérifie que l'ID de l'enseignant dans le DTO est null
        assertNull(result.getTeacher_id());
    }

}
