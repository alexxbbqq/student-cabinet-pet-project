package ru.university.studentapi.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
import ru.university.studentapi.dto.GradeHistoryEntryDto;
import ru.university.studentapi.dto.ScheduleDayDto;
import ru.university.studentapi.dto.StudentProfileDto;
import ru.university.studentapi.service.StudentCabinetService;

@RestController
@RequestMapping("/api/student")
public class StudentController {
    private final StudentCabinetService service;

    public StudentController(StudentCabinetService service) {
        this.service = service;
    }

    @GetMapping("/profile")
    public StudentProfileDto profile() {
        return service.getProfile();
    }

    @GetMapping("/grades")
    public List<GradeDto> grades() {
        return service.getGrades();
    }

    @GetMapping("/grades/history")
    public List<GradeHistoryEntryDto> gradeHistory() {
        return service.getGradeHistory();
    }

    @GetMapping("/schedule")
    public List<ScheduleDayDto> schedule() {
        return service.getSchedule();
    }

    @GetMapping("/debts")
    public List<DebtDto> debts() {
        return service.getDebts();
    }
}
