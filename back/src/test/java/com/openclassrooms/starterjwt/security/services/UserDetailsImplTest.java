package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {


    @Test
    public void AdminFieldTest() {    // Test le champ admin de la classe UserDetailsImpl
        // Création d'une instance UserDetailsImpl pour un utilisateur administrateur
        UserDetailsImpl adminUser = UserDetailsImpl.builder()
                .id(1L)
                .username("admin")
                .firstName("Admin")
                .lastName("User")
                .admin(true)
                .password("password")
                .build();

        // Création d'une instance UserDetailsImpl pour un utilisateur non-administrateur
        UserDetailsImpl nonAdminUser = UserDetailsImpl.builder()
                .id(2L)
                .username("user")
                .firstName("Non-admin")
                .lastName("User")
                .admin(false)
                .password("password")
                .build();

        // Vérifie que le champ admin est true pour l'utilisateur administrateur
        assertTrue(adminUser.getAdmin());
        // Vérifie que le champ admin est false pour l'utilisateur non-administrateur
        assertFalse(nonAdminUser.getAdmin());
    }

    @Test
    public void testEquals() { //Test que 2 instances pour voir si elles représentent le même utilisateur
        // Création de la première instance de UserDetailsImpl avec l'id 1
        UserDetailsImpl user1 = UserDetailsImpl.builder()
                .id(1L)
                .username("user1")
                .firstName("First")
                .lastName("User")
                .admin(true)  // Définit l'utilisateur comme administrateur
                .password("password")
                .build();

        // Création de la deuxième instance de UserDetailsImpl avec le même id mais des autres valeurs différentes
        UserDetailsImpl user2 = UserDetailsImpl.builder()
                .id(1L)
                .username("user2")
                .firstName("Second")
                .lastName("User")
                .admin(false)  // Définit l'utilisateur comme non-administrateur
                .password("password")
                .build();

        // Création de la troisième instance de UserDetailsImpl avec un id différent
        UserDetailsImpl user3 = UserDetailsImpl.builder()
                .id(2L)
                .username("user3")
                .firstName("Third")
                .lastName("User")
                .admin(true)  // Définit l'utilisateur comme administrateur
                .password("password")
                .build();

        // Vérification que deux utilisateurs avec le même id sont égaux
        assertEquals(user1, user2);

        // Vérification que deux utilisateurs avec des id différents ne sont pas égaux
        assertNotEquals(user1, user3);
    }


}
