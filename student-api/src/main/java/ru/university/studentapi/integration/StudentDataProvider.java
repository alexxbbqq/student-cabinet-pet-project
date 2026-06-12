package ru.university.studentapi.integration;

import java.util.List;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
import ru.university.studentapi.dto.ScheduleDayDto;
import ru.university.studentapi.dto.StudentProfileDto;

public interface StudentDataProvider {
    StudentProfileDto loadProfile(String studentId);
    List<GradeDto> loadGrades(String studentId);
    List<ScheduleDayDto> loadSchedule(String studentId);
    List<DebtDto> loadDebts(String studentId);
}
