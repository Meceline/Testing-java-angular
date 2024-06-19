package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    // Unitaire
    @Test
    public void getUserNameFromJwtToken() { // Teste la récupération du nom d'utilisateur à partir d'un token JWT
        JwtUtils jwtUtils = new JwtUtils();  // Crée une instance de JwtUtils
        String jwtSecret = "testSecret";  // Définit un secret JWT pour les tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);  // Injecte le champ jwtSecret dans l'instance de JwtUtils

        String username = "testUser";  // Nom d'utilisateur utilisé pour créer le token JWT

        // Crée un token JWT
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        // Appelle la méthode getUserNameFromJwtToken et vérifie que le nom d'utilisateur est correct
        String returnedUsername = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals(username, returnedUsername);  // Vérifie que le nom d'utilisateur extrait du token correspond au nom d'utilisateur initial
    }

    // Unitaire
    @Test
    public void testValidateJwtToken() { // Teste la validation d'un token JWT
        JwtUtils jwtUtils = new JwtUtils();  // Crée une instance de JwtUtils
        String jwtSecret = "testSecret";  // Définit un secret JWT pour les tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);  // Injecte le champ jwtSecret dans l'instance de JwtUtils

        String username = "testUser";  // Nom d'utilisateur utilisé pour créer le token JWT

        // Crée un token JWT valide qui expire dans 24 heures, signé avec le secret défini
        String validToken = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + 86400000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String invalidToken = "invalidToken";  // Définit un token JWT invalide

        // Vérifie que le token valide est reconnu comme valide
        boolean isValid = jwtUtils.validateJwtToken(validToken);
        // Vérifie que le token invalide est reconnu comme invalide
        boolean isInvalid = jwtUtils.validateJwtToken(invalidToken);

        assertTrue(isValid);  // Vérifie que le token valide est effectivement valide
        assertFalse(isInvalid);  // Vérifie que le token invalide est effectivement invalide
    }

    // Unitaire
    @Test
    public void testValidateJwtTokenWithInvalidSignature() { // Teste la validation d'un token JWT avec une mauvaise signature
        JwtUtils jwtUtils = new JwtUtils();  // Crée une instance de JwtUtils
        String jwtSecret = "testSecret";  // Définit un secret JWT pour les tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);  // Injecte le champ jwtSecret dans l'instance de JwtUtils

        // Crée un token JWT avec une mauvaise signature
        String invalidSignatureToken = Jwts.builder()
                .setSubject("testUser")
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));  // Vérifie que le token avec une mauvaise signature est invalide
    }
}
