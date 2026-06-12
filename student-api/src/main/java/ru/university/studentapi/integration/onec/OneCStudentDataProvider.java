package ru.university.studentapi.integration.onec;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
import ru.university.studentapi.dto.ScheduleDayDto;
import ru.university.studentapi.dto.StudentProfileDto;
import ru.university.studentapi.integration.StudentDataProvider;

@Component
@ConditionalOnProperty(name = "app.integration.onec.mode", havingValue = "http")
public class OneCStudentDataProvider implements StudentDataProvider {
    private final OneCProperties properties;
    private final RestTemplate restTemplate;

    public OneCStudentDataProvider(OneCProperties properties, RestTemplate restTemplate) {
        this.properties = properties;
        this.restTemplate = restTemplate;
    }

    @Override
    public StudentProfileDto loadProfile(String studentId) {
        return restTemplate.getForObject(url(studentId, "profile"), StudentProfileDto.class);
    }

    @Override
    public List<GradeDto> loadGrades(String studentId) {
        return list(url(studentId, "grades"), new ParameterizedTypeReference<List<GradeDto>>() {});
    }

    @Override
    public List<ScheduleDayDto> loadSchedule(String studentId) {
        return list(url(studentId, "schedule"), new ParameterizedTypeReference<List<ScheduleDayDto>>() {});
    }

    @Override
    public List<DebtDto> loadDebts(String studentId) {
        return list(url(studentId, "debts"), new ParameterizedTypeReference<List<DebtDto>>() {});
    }

    private String url(String studentId, String resource) {
        return properties.getBaseUrl() + "/onec/students/" + studentId + "/" + resource;
    }

    private <T> T list(String url, ParameterizedTypeReference<T> type) {
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, null, type);
        return response.getBody();
    }
}
