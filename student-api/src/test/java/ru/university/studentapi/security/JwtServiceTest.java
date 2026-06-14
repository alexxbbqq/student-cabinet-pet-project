package ru.university.studentapi.security;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() throws Exception {
        AuthProperties props = new AuthProperties();
        props.setJwtSecret("test-secret-for-unit-tests-minimum-32-chars");
        props.setTokenTtlMinutes(120);
        jwtService = new JwtService(new ObjectMapper(), props);
    }

    @Test
    void createToken_returnsThreePartJwt() {
        String token = jwtService.createToken("user123", "STUDENT");

        assertNotNull(token);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length, "JWT должен содержать три части: header.payload.signature");
    }

    @Test
    void validateAndGetSubject_validToken_returnsSubject() {
        String token = jwtService.createToken("2021-301-047", "STUDENT");

        String subject = jwtService.validateAndGetSubject(token);

        assertEquals("2021-301-047", subject);
    }

    @Test
    void validateAndGetSubject_tamperedSignature_returnsNull() {
        String token = jwtService.createToken("user123", "STUDENT");
        String tampered = token.substring(0, token.lastIndexOf('.') + 1) + "invalidsignature";

        assertNull(jwtService.validateAndGetSubject(tampered));
    }

    @Test
    void validateAndGetSubject_expiredToken_returnsNull() throws Exception {
        AuthProperties shortTtlProps = new AuthProperties();
        shortTtlProps.setJwtSecret("test-secret-for-unit-tests-minimum-32-chars");
        shortTtlProps.setTokenTtlMinutes(0);
        JwtService shortLivedService = new JwtService(new ObjectMapper(), shortTtlProps);

        // TTL=0 означает exp = now, токен немедленно истекает
        String token = shortLivedService.createToken("user123", "STUDENT");
        Thread.sleep(1100);

        assertNull(jwtService.validateAndGetSubject(token));
    }

    @Test
    void validateAndGetSubject_malformedToken_returnsNull() {
        assertNull(jwtService.validateAndGetSubject("not.a.valid.jwt.token"));
        assertNull(jwtService.validateAndGetSubject("onlytwoparts.here"));
        assertNull(jwtService.validateAndGetSubject(""));
        assertNull(jwtService.validateAndGetSubject(null));
    }

    @Test
    void validateAndGetSubject_wrongSecret_returnsNull() throws Exception {
        AuthProperties otherProps = new AuthProperties();
        otherProps.setJwtSecret("completely-different-secret-32chars!!");
        otherProps.setTokenTtlMinutes(120);
        JwtService otherService = new JwtService(new ObjectMapper(), otherProps);

        String token = otherService.createToken("user123", "STUDENT");

        assertNull(jwtService.validateAndGetSubject(token));
    }
}
