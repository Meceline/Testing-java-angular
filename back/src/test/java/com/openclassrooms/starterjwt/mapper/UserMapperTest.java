package com.openclassrooms.starterjwt.mapper;


import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class UserMapperTest {
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    public void toEntity() {
        // Crée un DTO utilisateur
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(true);
        userDto.setPassword("password");
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        // Convertit le DTO en entité
        User user = userMapper.toEntity(userDto);

        // Vérifie que les champs sont mappés correctement
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }

    @Test
    public void toDto() {
        // Crée une entité utilisateur
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(true);
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Convertit l'entité en DTO
        UserDto userDto = userMapper.toDto(user);

        // Vérifie que les champs sont mappés correctement
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }
    @Test
    public void toEntityList() {
        ArrayList<UserDto> list = null;
        // Crée un DTO utilisateur
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("test@example.com");
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setAdmin(true);
        userDto.setPassword("password");
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());

        List<User> userList = userMapper.toEntity(list);
        assertNull(userList);
        list = new ArrayList<>();
        list.add(userDto);
        // Convertit le DTO en entité
        userList = userMapper.toEntity(list);
        User user = userList.get(0);
        // Vérifie que les champs sont mappés correctement
        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getEmail(), user.getEmail());
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.isAdmin(), user.isAdmin());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt());
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt());
    }
    @Test
    public void toDtoList() {
        List<User> list = null;
        // Crée une entité utilisateur
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAdmin(true);
        user.setPassword("password");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Convertit l'entité en DTO
        List<UserDto> userDtoList = userMapper.toDto(list);
        assertNull(list);
        list = new ArrayList<>();
        list.add(user);
        userDtoList = userMapper.toDto(list);

        UserDto userDto = userDtoList.get(0);

        // Vérifie que les champs sont mappés correctement
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertEquals(user.isAdmin(), userDto.isAdmin());
        assertEquals(user.getPassword(), userDto.getPassword());
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt());
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt());
    }


}
