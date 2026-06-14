package ru.university.studentapi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
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
import ru.university.studentapi.repository.GradeAuditLogRepository;
import ru.university.studentapi.repository.GradeRepository;
import ru.university.studentapi.repository.ScheduleDayRepository;
import ru.university.studentapi.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentCabinetServiceTest {

    @Mock private AppUserRepository appUserRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private GradeRepository gradeRepository;
    @Mock private ScheduleDayRepository scheduleDayRepository;
    @Mock private DebtRepository debtRepository;
    @Mock private GradeAuditLogRepository gradeAuditLogRepository;

    @InjectMocks
    private StudentCabinetService service;

    private AppUserEntity appUser;
    private StudentEntity student;

    @BeforeEach
    void setUp() {
        appUser = new AppUserEntity();
        appUser.setLogin("2021-301-047");
        appUser.setStudentOnecId("2021-301-047");
        appUser.setActive(true);

        student = new StudentEntity();
        student.setId(UUID.randomUUID());
        student.setOnecId("2021-301-047");
        student.setFullName("Иванов Алексей Игоревич");
        student.setInitials("АИ");
        student.setGroupName("ИТ-301");
        student.setCourse(3);
        student.setEmail("a.ivanov@student.kosygin-rgu.ru");
        student.setPhone("+7 (916) 123-45-67");
        student.setBirthDate("14 марта 2002");
        student.setFaculty("Институт мехатроники");
        student.setDirection("09.03.04 Программная инженерия");
        student.setCurator("Смирнова О.П.");
        student.setYearIn(2021);
        student.setYearOut(2025);
        student.setRecordBook("2021-301-047");
        student.setEducationForm("Очная, бюджет");

        // Имитируем аутентифицированного пользователя в SecurityContext
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("2021-301-047", null, Collections.emptyList())
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // --- getProfile ---

    @Test
    void getProfile_returnsCorrectDto() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));

        StudentProfileDto dto = service.getProfile();

        assertEquals("Иванов Алексей Игоревич", dto.getFullName());
        assertEquals("АИ", dto.getInitials());
        assertEquals("ИТ-301", dto.getGroupName());
        assertEquals(3, dto.getCourse());
        assertEquals("a.ivanov@student.kosygin-rgu.ru", dto.getEmail());
        assertEquals("+7 (916) 123-45-67", dto.getPhone());
        assertEquals("14 марта 2002", dto.getBirthDate());
        assertEquals("2021-301-047", dto.getRecordBook());
        assertEquals(2021, dto.getYearIn());
        assertEquals(2025, dto.getYearOut());
    }

    @Test
    void getProfile_studentNotSynced_throwsIllegalState() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.getProfile());
    }

    @Test
    void getProfile_userNotLinkedToStudent_throwsIllegalState() {
        appUser.setStudentOnecId(null);
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));

        assertThrows(IllegalStateException.class, () -> service.getProfile());
    }

    // --- getGrades ---

    @Test
    void getGrades_returnsCorrectDtos() {
        GradeEntity grade = new GradeEntity();
        grade.setOnecId("grade-1");
        grade.setSubject("Базы данных");
        grade.setTeacher("Иванов А.В.");
        grade.setType("exam");
        grade.setTypeLabel("Экзамен");
        grade.setGradeDateLabel("18 янв");
        grade.setGradeValue("5");
        grade.setStatus("done");

        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));
        when(gradeRepository.findByStudentOrderBySubjectAsc(student)).thenReturn(List.of(grade));

        List<GradeDto> result = service.getGrades();

        assertEquals(1, result.size());
        GradeDto dto = result.get(0);
        assertEquals("grade-1", dto.getId());
        assertEquals("Базы данных", dto.getSubject());
        assertEquals("Иванов А.В.", dto.getTeacher());
        assertEquals("exam", dto.getType());
        assertEquals("Экзамен", dto.getTypeLabel());
        assertEquals("18 янв", dto.getDate());
        assertEquals("5", dto.getGrade());
        assertEquals("done", dto.getStatus());
    }

    @Test
    void getGrades_emptyList_returnsEmpty() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));
        when(gradeRepository.findByStudentOrderBySubjectAsc(student)).thenReturn(Collections.emptyList());

        assertTrue(service.getGrades().isEmpty());
    }

    @Test
    void getGrades_studentNotSynced_throwsIllegalState() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.getGrades());
    }

    // --- getSchedule ---

    @Test
    void getSchedule_returnsCorrectDtos() {
        LessonEntity lesson = new LessonEntity();
        lesson.setLessonTime("8:30-10:00");
        lesson.setName("Базы данных");
        lesson.setRoom("ауд. 304");
        lesson.setType("lecture");

        ScheduleDayEntity day = new ScheduleDayEntity();
        day.setDayCode("Пн");
        day.setDayNumber(27);
        day.addLesson(lesson);

        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));
        when(scheduleDayRepository.findByStudentOrderBySortOrderAsc(student)).thenReturn(List.of(day));

        List<ScheduleDayDto> result = service.getSchedule();

        assertEquals(1, result.size());
        ScheduleDayDto dto = result.get(0);
        assertEquals("Пн", dto.getDay());
        assertEquals(27, dto.getDate());
        assertEquals(1, dto.getLessons().size());
        assertEquals("8:30-10:00", dto.getLessons().get(0).getTime());
        assertEquals("Базы данных", dto.getLessons().get(0).getName());
        assertEquals("ауд. 304", dto.getLessons().get(0).getRoom());
        assertEquals("lecture", dto.getLessons().get(0).getType());
    }

    @Test
    void getSchedule_dayWithNoLessons_returnsDayWithEmptyList() {
        ScheduleDayEntity emptyDay = new ScheduleDayEntity();
        emptyDay.setDayCode("Сб");
        emptyDay.setDayNumber(1);

        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));
        when(scheduleDayRepository.findByStudentOrderBySortOrderAsc(student)).thenReturn(List.of(emptyDay));

        List<ScheduleDayDto> result = service.getSchedule();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getLessons().isEmpty());
    }

    @Test
    void getSchedule_studentNotSynced_throwsIllegalState() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.getSchedule());
    }

    // --- getDebts ---

    @Test
    void getDebts_returnsCorrectDtos() {
        DebtEntity debt = new DebtEntity();
        debt.setOnecId("debt-1");
        debt.setSubject("Математический анализ");
        debt.setTeacher("Козлов Д.Р.");
        debt.setReason("Экзамен не сдан");
        debt.setRetakeDate("14 февраля 2025, 10:00");
        debt.setLocation("ауд. 108");

        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));
        when(debtRepository.findByStudentOrderBySubjectAsc(student)).thenReturn(List.of(debt));

        List<DebtDto> result = service.getDebts();

        assertEquals(1, result.size());
        DebtDto dto = result.get(0);
        assertEquals("debt-1", dto.getId());
        assertEquals("Математический анализ", dto.getSubject());
        assertEquals("Козлов Д.Р.", dto.getTeacher());
        assertEquals("Экзамен не сдан", dto.getReason());
        assertEquals("14 февраля 2025, 10:00", dto.getRetakeDate());
        assertEquals("ауд. 108", dto.getLocation());
    }

    @Test
    void getDebts_noDebts_returnsEmpty() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.of(student));
        when(debtRepository.findByStudentOrderBySubjectAsc(student)).thenReturn(Collections.emptyList());

        assertTrue(service.getDebts().isEmpty());
    }

    @Test
    void getDebts_studentNotSynced_throwsIllegalState() {
        when(appUserRepository.findByLogin("2021-301-047")).thenReturn(Optional.of(appUser));
        when(studentRepository.findByOnecId("2021-301-047")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> service.getDebts());
    }
}
