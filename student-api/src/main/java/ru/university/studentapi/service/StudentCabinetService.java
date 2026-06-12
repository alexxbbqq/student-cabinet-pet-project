package ru.university.studentapi.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
import ru.university.studentapi.dto.LessonDto;
import ru.university.studentapi.dto.ScheduleDayDto;
import ru.university.studentapi.dto.StudentProfileDto;
import ru.university.studentapi.entity.AppUserEntity;
import ru.university.studentapi.entity.DebtEntity;
import ru.university.studentapi.entity.GradeEntity;
import ru.university.studentapi.entity.LessonEntity;
import ru.university.studentapi.entity.ScheduleDayEntity;
import ru.university.studentapi.entity.StudentEntity;
import ru.university.studentapi.repository.AppUserRepository;
import ru.university.studentapi.repository.DebtRepository;
import ru.university.studentapi.repository.GradeRepository;
import ru.university.studentapi.repository.ScheduleDayRepository;
import ru.university.studentapi.repository.StudentRepository;

@Service
public class StudentCabinetService {
    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final DebtRepository debtRepository;

    public StudentCabinetService(
            AppUserRepository appUserRepository,
            StudentRepository studentRepository,
            GradeRepository gradeRepository,
            ScheduleDayRepository scheduleDayRepository,
            DebtRepository debtRepository) {
        this.appUserRepository = appUserRepository;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.scheduleDayRepository = scheduleDayRepository;
        this.debtRepository = debtRepository;
    }

    @Transactional(readOnly = true)
    public StudentProfileDto getProfile() {
        StudentEntity student = getCurrentStudent();
        StudentProfileDto dto = new StudentProfileDto();
        dto.setFullName(student.getFullName());
        dto.setInitials(student.getInitials());
        dto.setGroupName(student.getGroupName());
        dto.setCourse(student.getCourse());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setBirthDate(student.getBirthDate());
        dto.setFaculty(student.getFaculty());
        dto.setDirection(student.getDirection());
        dto.setCurator(student.getCurator());
        dto.setYearIn(student.getYearIn());
        dto.setYearOut(student.getYearOut());
        dto.setRecordBook(student.getRecordBook());
        dto.setEducationForm(student.getEducationForm());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<GradeDto> getGrades() {
        List<GradeDto> result = new ArrayList<>();
        for (GradeEntity entity : gradeRepository.findByStudentOrderBySubjectAsc(getCurrentStudent())) {
            result.add(new GradeDto(
                    entity.getOnecId(),
                    entity.getSubject(),
                    entity.getTeacher(),
                    entity.getType(),
                    entity.getTypeLabel(),
                    entity.getGradeDateLabel(),
                    entity.getGradeValue(),
                    entity.getStatus()));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ScheduleDayDto> getSchedule() {
        List<ScheduleDayDto> result = new ArrayList<>();
        for (ScheduleDayEntity day : scheduleDayRepository.findByStudentOrderBySortOrderAsc(getCurrentStudent())) {
            List<LessonDto> lessons = new ArrayList<>();
            for (LessonEntity lesson : day.getLessons()) {
                lessons.add(new LessonDto(
                        lesson.getLessonTime(),
                        lesson.getName(),
                        lesson.getRoom(),
                        lesson.getType()));
            }
            result.add(new ScheduleDayDto(day.getDayCode(), day.getDayNumber(), lessons));
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<DebtDto> getDebts() {
        List<DebtDto> result = new ArrayList<>();
        for (DebtEntity entity : debtRepository.findByStudentOrderBySubjectAsc(getCurrentStudent())) {
            result.add(new DebtDto(
                    entity.getOnecId(),
                    entity.getSubject(),
                    entity.getTeacher(),
                    entity.getReason(),
                    entity.getRetakeDate(),
                    entity.getLocation()));
        }
        return result;
    }

    private StudentEntity getCurrentStudent() {
        String studentOnecId = getCurrentUser()
                .getStudentOnecId();
        if (studentOnecId == null || studentOnecId.trim().isEmpty()) {
            throw new IllegalStateException("Current user is not linked to a student record");
        }
        return studentRepository.findByOnecId(studentOnecId)
                .orElseThrow(() -> new IllegalStateException("Student data is not synchronized yet"));
    }

    private AppUserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new IllegalStateException("Current user is not authenticated");
        }
        return appUserRepository.findByLogin(authentication.getName())
                .filter(user -> Boolean.TRUE.equals(user.getActive()))
                .orElseThrow(() -> new IllegalStateException("Current user was not found"));
    }
}

