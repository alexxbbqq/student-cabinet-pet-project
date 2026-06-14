package ru.university.studentapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // --- 401 без токена ---

    @Test
    void getProfile_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/student/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getGrades_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/student/grades"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getSchedule_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/student/schedule"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getDebts_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/student/debts"))
                .andExpect(status().isUnauthorized());
    }

    // --- 200 с валидным токеном ---

    @Test
    void getProfile_withValidToken_returns200() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/student/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").isString())
                .andExpect(jsonPath("$.groupName").isString())
                .andExpect(jsonPath("$.initials").isString());
    }

    @Test
    void getGrades_withValidToken_returns200() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/student/grades")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getSchedule_withValidToken_returns200() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/student/schedule")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getDebts_withValidToken_returns200() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(get("/api/student/debts")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // --- Прочие сценарии ---

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"2021-301-047\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"2021-301-047\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.login").value("2021-301-047"));
    }

    @Test
    void getProfile_withInvalidToken_returns401() throws Exception {
        mockMvc.perform(get("/api/student/profile")
                        .header("Authorization", "Bearer this.is.not.valid"))
                .andExpect(status().isUnauthorized());
    }

    // --- Вспомогательный метод ---

    private String loginAndGetToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":\"2021-301-047\",\"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        JsonNode json = objectMapper.readTree(body);
        return json.get("token").asText();
    }
}
