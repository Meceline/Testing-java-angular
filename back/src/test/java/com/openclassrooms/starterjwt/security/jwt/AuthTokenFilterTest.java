package com.openclassrooms.starterjwt.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AuthTokenFilterTest {
    @InjectMocks
    AuthTokenFilter authTokenFilter;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserDetailsService userDetailsService;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    FilterChain filterChain;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // Initialise les mocks annotés avec @Mock
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);  // Crée un mock pour SecurityContext
        SecurityContextHolder.setContext(securityContext);  // Définit le SecurityContext mocké dans le SecurityContextHolder
    }



    @Test
    public void doFilterInternal() throws Exception { // Vérifie que doFilterInternal fonctionne correctement avec un en-tête Authorization valide
        when(request.getHeader("Authorization")).thenReturn("Bearer token");  // Simule l'en-tête Authorization avec un token JWT
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);  // Simule la validation réussie du token JWT
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username");  // Simule l'extraction du nom d'utilisateur à partir du token JWT
        UserDetails userDetails = mock(UserDetails.class);  // Crée un mock de UserDetails
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);  // Simule le chargement des détails de l'utilisateur

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }


    @Test
    public void doFilterInternal_NoAuthorizationHeader() throws Exception { // Vérifie que doFilterInternal fonctionne correctement lorsque l'en-tête Authorization est absent
        when(request.getHeader("Authorization")).thenReturn(null);  // Simule l'absence de l'en-tête Authorization

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }


    @Test
    public void doFilterInternal_AuthorizationHeaderNotStartingWithBearer() throws Exception { // Vérifie que doFilterInternal fonctionne correctement lorsque l'en-tête Authorization ne commence pas par "Bearer"
        when(request.getHeader("Authorization")).thenReturn("Invalid token");  // Simule un en-tête Authorization invalide

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }


    @Test
    public void doFilterInternal_InvalidJwtToken() throws Exception {  // Vérifie que doFilterInternal fonctionne correctement lorsque le token JWT est invalide
        when(request.getHeader("Authorization")).thenReturn("Bearer token");  // Simule un en-tête Authorization avec un token JWT
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(false);  // Simule l'échec de la validation du token JWT

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }


    @Test
    public void doFilterInternal_ExceptionWhenLoadingUserByUsername() throws Exception { // Vérifie que doFilterInternal fonctionne correctement lorsqu'une exception est levée lors du chargement des détails de l'utilisateur
        when(request.getHeader("Authorization")).thenReturn("Bearer token");  // Simule un en-tête Authorization avec un token JWT
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);  // Simule la validation réussie du token JWT
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username");  // Simule l'extraction du nom d'utilisateur à partir du token JWT
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));  // Simule une exception lors du chargement des détails de l'utilisateur

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }

    @Test
    public void doFilterInternal_ExpiredJwtToken() throws Exception { //Test pour un token expriré
        when(request.getHeader("Authorization")).thenReturn("Bearer expiredToken");  // Simule un en-tête Authorization avec un token JWT expiré
        when(jwtUtils.validateJwtToken(anyString())).thenThrow(new ExpiredJwtException(null, null, "Token expired"));  // Simule un token expiré

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }

    @Test
    public void doFilterInternal_UserDetailsNotFound() throws Exception { //UserDetail manquant
        when(request.getHeader("Authorization")).thenReturn("Bearer token");  // Simule un en-tête Authorization avec un token JWT
        when(jwtUtils.validateJwtToken(anyString())).thenReturn(true);  // Simule la validation réussie du token JWT
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("username");  // Simule l'extraction du nom d'utilisateur à partir du token JWT
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("User not found"));  // Simule un UserDetails non trouvé

        authTokenFilter.doFilterInternal(request, response, filterChain);  // Exécute la méthode à tester

        verify(filterChain, times(1)).doFilter(request, response);  // Vérifie que le filtre a bien passé la requête et la réponse au filtre suivant dans la chaîne
    }



}
