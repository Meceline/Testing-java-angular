package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsImplTest {

    // Unitaire
    @Test
    public void AdminFieldTest() { // Teste le champ admin de la classe UserDetailsImpl

        UserDetailsImpl adminUser = UserDetailsImpl.builder() // Création d'une instance UserDetailsImpl pour un utilisateur administrateur
                .id(1L)
                .username("admin")
                .firstName("Admin")
                .lastName("User")
                .admin(true)  // Définit l'utilisateur comme administrateur
                .password("password")
                .build();

        UserDetailsImpl nonAdminUser = UserDetailsImpl.builder() // Création d'une instance UserDetailsImpl pour un utilisateur non-administrateur
                .id(2L)
                .username("user")
                .firstName("Non-admin")
                .lastName("User")
                .admin(false)  // Définit l'utilisateur comme non-administrateur
                .password("password")
                .build();

        assertTrue(adminUser.getAdmin());  // Vérifie que l'utilisateur admin a le champ admin défini sur true
        assertFalse(nonAdminUser.getAdmin());  // Vérifie que l'utilisateur non-admin a le champ admin défini sur false
    }

    // Unitaire
    @Test
    public void testEquals() { // Teste que deux instances de UserDetailsImpl représentent le même utilisateur
        UserDetailsImpl user1 = UserDetailsImpl.builder() // Création de la première instance de UserDetailsImpl avec l'id 1
                .id(1L)
                .username("user1")
                .firstName("First")
                .lastName("User")
                .admin(true)  // Définit l'utilisateur comme administrateur
                .password("password")
                .build();


        UserDetailsImpl user2 = UserDetailsImpl.builder() // Création de la deuxième instance de UserDetailsImpl avec le même id mais des valeurs différentes
                .id(1L)
                .username("user2")
                .firstName("Second")
                .lastName("User")
                .admin(false)  // Définit l'utilisateur comme non-administrateur
                .password("password")
                .build();


        UserDetailsImpl user3 = UserDetailsImpl.builder() // Création de la troisième instance de UserDetailsImpl avec un id différent
                .id(2L)
                .username("user3")
                .firstName("Third")
                .lastName("User")
                .admin(true)  // Définit l'utilisateur comme administrateur
                .password("password")
                .build();

        assertEquals(user1, user2);  // Vérifie que les utilisateurs avec le même id sont égaux
        assertNotEquals(user1, user3);  // Vérifie que les utilisateurs avec des id différents ne sont pas égaux
    }

}
