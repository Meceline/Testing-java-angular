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

    // Unitaire
    @Test
    public void toEntity() { // Convertit un UserDto en User
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
        assertEquals(userDto.getId(), user.getId()); // Vérifie que l'ID est mappé correctement
        assertEquals(userDto.getEmail(), user.getEmail()); // Vérifie que l'email est mappé correctement
        assertEquals(userDto.getFirstName(), user.getFirstName()); // Vérifie que le prénom est mappé correctement
        assertEquals(userDto.getLastName(), user.getLastName()); // Vérifie que le nom de famille est mappé correctement
        assertEquals(userDto.isAdmin(), user.isAdmin()); // Vérifie que le statut d'admin est mappé correctement
        assertEquals(userDto.getPassword(), user.getPassword()); // Vérifie que le mot de passe est mappé correctement
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt()); // Vérifie que la date de création est mappée correctement
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt()); // Vérifie que la date de mise à jour est mappée correctement
    }

    // Unitaire
    @Test
    public void toDto() { // Convertit un User en UserDto
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
        assertEquals(user.getId(), userDto.getId()); // Vérifie que l'ID est mappé correctement
        assertEquals(user.getEmail(), userDto.getEmail()); // Vérifie que l'email est mappé correctement
        assertEquals(user.getFirstName(), userDto.getFirstName()); // Vérifie que le prénom est mappé correctement
        assertEquals(user.getLastName(), userDto.getLastName()); // Vérifie que le nom de famille est mappé correctement
        assertEquals(user.isAdmin(), userDto.isAdmin()); // Vérifie que le statut d'admin est mappé correctement
        assertEquals(user.getPassword(), userDto.getPassword()); // Vérifie que le mot de passe est mappé correctement
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt()); // Vérifie que la date de création est mappée correctement
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt()); // Vérifie que la date de mise à jour est mappée correctement
    }

    // Unitaire
    @Test
    public void toEntityList() { // Convertit une liste de UserDto en liste de User
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
        assertNull(userList); // Vérifie que la conversion de null renvoie null
        list = new ArrayList<>();
        list.add(userDto);
        // Convertit la liste de DTO en entités
        userList = userMapper.toEntity(list);
        User user = userList.get(0);
        // Vérifie que les champs sont mappés correctement
        assertEquals(userDto.getId(), user.getId()); // Vérifie que l'ID est mappé correctement
        assertEquals(userDto.getEmail(), user.getEmail()); // Vérifie que l'email est mappé correctement
        assertEquals(userDto.getFirstName(), user.getFirstName()); // Vérifie que le prénom est mappé correctement
        assertEquals(userDto.getLastName(), user.getLastName()); // Vérifie que le nom de famille est mappé correctement
        assertEquals(userDto.isAdmin(), user.isAdmin()); // Vérifie que le statut d'admin est mappé correctement
        assertEquals(userDto.getPassword(), user.getPassword()); // Vérifie que le mot de passe est mappé correctement
        assertEquals(userDto.getCreatedAt(), user.getCreatedAt()); // Vérifie que la date de création est mappée correctement
        assertEquals(userDto.getUpdatedAt(), user.getUpdatedAt()); // Vérifie que la date de mise à jour est mappée correctement
    }

    // Unitaire
    @Test
    public void toDtoList() { // Convertit une liste de User en liste de UserDto
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
        assertNull(userDtoList); // Vérifie que la conversion de null renvoie null
        list = new ArrayList<>();
        list.add(user);
        userDtoList = userMapper.toDto(list);

        UserDto userDto = userDtoList.get(0);

        // Vérifie que les champs sont mappés correctement
        assertEquals(user.getId(), userDto.getId()); // Vérifie que l'ID est mappé correctement
        assertEquals(user.getEmail(), userDto.getEmail()); // Vérifie que l'email est mappé correctement
        assertEquals(user.getFirstName(), userDto.getFirstName()); // Vérifie que le prénom est mappé correctement
        assertEquals(user.getLastName(), userDto.getLastName()); // Vérifie que le nom de famille est mappé correctement
        assertEquals(user.isAdmin(), userDto.isAdmin()); // Vérifie que le statut d'admin est mappé correctement
        assertEquals(user.getPassword(), userDto.getPassword()); // Vérifie que le mot de passe est mappé correctement
        assertEquals(user.getCreatedAt(), userDto.getCreatedAt()); // Vérifie que la date de création est mappée correctement
        assertEquals(user.getUpdatedAt(), userDto.getUpdatedAt()); // Vérifie que la date de mise à jour est mappée correctement
    }
}
