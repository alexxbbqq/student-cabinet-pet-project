package ru.university.studentapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.university.studentapi.entity.AppUserEntity;
import ru.university.studentapi.repository.AppUserRepository;
import ru.university.studentapi.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private AppUserEntity activeUser;

    @BeforeEach
    void setUp() {
        activeUser = new AppUserEntity();
        activeUser.setId(UUID.randomUUID());
        activeUser.setLogin("2021-301-047");
        activeUser.setPasswordHash("$2a$10$hashedpassword");
        activeUser.setRole("STUDENT");
        activeUser.setStudentOnecId("2021-301-047");
        activeUser.setActive(true);
    }

    @Test
    void login_correctCredentials_returnsToken() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("password", activeUser.getPasswordHash())).thenReturn(true);
        when(jwtService.createToken("2021-301-047", "STUDENT")).thenReturn("jwt-token-value");

        Optional<String> result = authService.login("2021-301-047", "password");

        assertTrue(result.isPresent());
        assertEquals("jwt-token-value", result.get());
    }

    @Test
    void login_wrongPassword_returnsEmpty() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(activeUser));
        when(passwordEncoder.matches("wrongpass", activeUser.getPasswordHash())).thenReturn(false);

        Optional<String> result = authService.login("2021-301-047", "wrongpass");

        assertTrue(result.isEmpty());
        verify(jwtService, never()).createToken(any(), any());
    }

    @Test
    void login_unknownUser_returnsEmpty() {
        when(appUserRepository.findByLogin("unknown")).thenReturn(Optional.empty());

        Optional<String> result = authService.login("unknown", "password");

        assertTrue(result.isEmpty());
        verify(jwtService, never()).createToken(any(), any());
    }

    @Test
    void login_inactiveUser_returnsEmpty() {
        activeUser.setActive(false);
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(activeUser));

        Optional<String> result = authService.login("2021-301-047", "password");

        assertTrue(result.isEmpty());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).createToken(any(), any());
    }
}
