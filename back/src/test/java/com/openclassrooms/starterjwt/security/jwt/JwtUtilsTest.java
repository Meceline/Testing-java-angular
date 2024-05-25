package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    @Test
    public void getUserNameFromJwtToken() {
        JwtUtils jwtUtils = new JwtUtils();
        String jwtSecret = "testSecret";
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);  // Utilise ReflectionTestUtils pour injecter le champ jwtSecret dans l'instance de JwtUtils

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


    @Test
    public void testValidateJwtToken() {
        JwtUtils jwtUtils = new JwtUtils();
        String jwtSecret = "testSecret";  // Définit un secret JWT pour les tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);  // Utilise ReflectionTestUtils pour injecter le champ jwtSecret dans l'instance de JwtUtils

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

        assertTrue(isValid);  // Assert que le token valide est effectivement valide
        assertFalse(isInvalid);  // Assert que le token invalide est effectivement invalide
    }


    @Test
    public void testValidateJwtTokenWithInvalidSignature() {
        JwtUtils jwtUtils = new JwtUtils();
        String jwtSecret = "testSecret";  // Définit un secret JWT pour les tests
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", jwtSecret);  // Utilise ReflectionTestUtils pour injecter le champ jwtSecret dans l'instance de JwtUtils

        // Crée un token JWT avec une mauvaise signature
        String invalidSignatureToken = Jwts.builder()
                .setSubject("testUser")
                .signWith(SignatureAlgorithm.HS512, "wrongSecret")
                .compact();

        assertFalse(jwtUtils.validateJwtToken(invalidSignatureToken));  // Vérifie que le token avec une mauvaise signature est invalide
    }

}
