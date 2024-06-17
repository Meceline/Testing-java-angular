package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherMapperTest {

    private TeacherMapper teacherMapper;

    @BeforeEach
    public void setUp() {
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
    }

    @Test
    public void toEntity() {
        Teacher nullTeacher = teacherMapper.toEntity((TeacherDto) null);
        assertNull(nullTeacher);

        // Crée un DTO enseignant pour le test
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        teacherDto.setCreatedAt(LocalDateTime.now());
        teacherDto.setUpdatedAt(LocalDateTime.now());

        // Convertit le DTO en entité
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        // Vérifie que les champs sont mappés correctement
        assertEquals(teacherDto.getId(), teacher.getId());
        assertEquals(teacherDto.getFirstName(), teacher.getFirstName());
        assertEquals(teacherDto.getLastName(), teacher.getLastName());
        assertEquals(teacherDto.getCreatedAt(), teacher.getCreatedAt());
        assertEquals(teacherDto.getUpdatedAt(), teacher.getUpdatedAt());
    }


    @Test
    public void toDto() {
        TeacherDto nullTeacher = teacherMapper.toDto((Teacher) null);
        assertNull(nullTeacher);

        // Crée une entité enseignant pour le test
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setCreatedAt(LocalDateTime.now());
        teacher.setUpdatedAt(LocalDateTime.now());

        // Convertit l'entité en DTO
        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        // Vérifie que les champs sont mappés correctement
        assertEquals(teacher.getId(), teacherDto.getId());
        assertEquals(teacher.getFirstName(), teacherDto.getFirstName());
        assertEquals(teacher.getLastName(), teacherDto.getLastName());
        assertEquals(teacher.getCreatedAt(), teacherDto.getCreatedAt());
        assertEquals(teacher.getUpdatedAt(), teacherDto.getUpdatedAt());
    }

    @Test
    public void toEntity_withNullList_returnsNull() {
        List<Teacher> result = teacherMapper.toEntity((List<TeacherDto>) null);

        // Vérifie que le résultat est null
        assertNull(result);
    }

    @Test
    public void toEntity_withEmptyList_returnsEmptyList() {
        // Passe une liste vide à la méthode toEntity
        List<TeacherDto> emptyList = new ArrayList<>();
        List<Teacher> result = teacherMapper.toEntity(emptyList);

        // Vérifie que le résultat est une liste vide
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void toEntity_withValidList_returnsEntityList() {
        // DTOs valides
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        dto1.setLastName("Doe");
        dto1.setFirstName("John");
        dto1.setCreatedAt(LocalDateTime.now());
        dto1.setUpdatedAt(LocalDateTime.now());

        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dto2.setLastName("Smith");
        dto2.setFirstName("Jane");
        dto2.setCreatedAt(LocalDateTime.now());
        dto2.setUpdatedAt(LocalDateTime.now());

        List<TeacherDto> dtoList = Arrays.asList(dto1, dto2);

        // Convertit la liste de DTOs en liste d'entités
        List<Teacher> result = teacherMapper.toEntity(dtoList);

        // Vérifie que le résultat n'est pas null et que la taille de la liste ok
        assertNotNull(result);
        assertEquals(2, result.size());

        // Vérifie que les propriétés des éléments sont correctement mappées
        Teacher teacher1 = result.get(0);
        assertEquals(dto1.getId(), teacher1.getId());
        assertEquals(dto1.getLastName(), teacher1.getLastName());
        assertEquals(dto1.getFirstName(), teacher1.getFirstName());
        assertEquals(dto1.getCreatedAt(), teacher1.getCreatedAt());
        assertEquals(dto1.getUpdatedAt(), teacher1.getUpdatedAt());

        Teacher teacher2 = result.get(1);
        assertEquals(dto2.getId(), teacher2.getId());
        assertEquals(dto2.getLastName(), teacher2.getLastName());
        assertEquals(dto2.getFirstName(), teacher2.getFirstName());
        assertEquals(dto2.getCreatedAt(), teacher2.getCreatedAt());
        assertEquals(dto2.getUpdatedAt(), teacher2.getUpdatedAt());
    }

}
