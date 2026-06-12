package ru.university.studentapi.service;

import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.university.studentapi.entity.AppUserEntity;
import ru.university.studentapi.repository.AppUserRepository;
import ru.university.studentapi.security.JwtService;

@Service
public class AuthService {
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AppUserRepository appUserRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<String> login(String login, String password) {
        return appUserRepository.findByLogin(login)
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .filter(user -> passwordEncoder.matches(password, user.getPasswordHash()))
                .map(user -> jwtService.createToken(user.getLogin(), user.getRole()));
    }
}
