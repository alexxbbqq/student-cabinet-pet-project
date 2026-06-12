package ru.university.studentapi.integration.onec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
import ru.university.studentapi.dto.LessonDto;
import ru.university.studentapi.dto.ScheduleDayDto;
import ru.university.studentapi.dto.StudentProfileDto;
import ru.university.studentapi.integration.StudentDataProvider;

@Component
@ConditionalOnProperty(name = "app.integration.onec.mode", havingValue = "mock", matchIfMissing = true)
public class MockOneCStudentDataProvider implements StudentDataProvider {
    @Override
    public StudentProfileDto loadProfile(String studentId) {
        StudentProfileDto profile = new StudentProfileDto();
        profile.setFullName("Иванов Алексей Игоревич");
        profile.setInitials("АИ");
        profile.setGroupName("ИТ-301");
        profile.setCourse(3);
        profile.setEmail("a.ivanov@student.kosygin-rgu.ru");
        profile.setPhone("+7 (916) 123-45-67");
        profile.setBirthDate("14 марта 2002");
        profile.setFaculty("Институт мехатроники и информационных технологий");
        profile.setDirection("09.03.04 Программная инженерия");
        profile.setCurator("Смирнова О.П.");
        profile.setYearIn(2021);
        profile.setYearOut(2025);
        profile.setRecordBook("2021-301-047");
        profile.setEducationForm("Очная, бюджет");
        return profile;
    }

    @Override
    public List<GradeDto> loadGrades(String studentId) {
        return Arrays.asList(
                new GradeDto("grade-1", "Базы данных", "Иванов А.В.", "exam", "Экзамен", "18 янв", "5", "done"),
                new GradeDto("grade-2", "Операционные системы", "Петров С.Н.", "exam", "Экзамен", "20 янв", "4", "done"),
                new GradeDto("grade-3", "Алгоритмы и структуры данных", "Смирнова О.П.", "exam", "Экзамен", "22 янв", "5", "done"),
                new GradeDto("grade-4", "Математический анализ", "Козлов Д.Р.", "exam", "Экзамен", "25 янв", "2", "debt"),
                new GradeDto("grade-5", "Сети и телекоммуникации", "Миронов С.А.", "exam", "Экзамен", "30 янв", null, "pending"),
                new GradeDto("grade-6", "Проектирование ПО", "Лебедева Н.К.", "exam", "Экзамен", "2 фев", null, "pending"),
                new GradeDto("grade-7", "Web-разработка", "Орлов М.Р.", "credit", "Зачет", "15 янв", "зач", "done"),
                new GradeDto("grade-8", "Английский язык", "Никитина Е.В.", "credit", "Зачет", "16 янв", "зач", "done")
        );
    }

    @Override
    public List<ScheduleDayDto> loadSchedule(String studentId) {
        return Arrays.asList(
                new ScheduleDayDto("Пн", 27, Arrays.asList(
                        new LessonDto("8:30-10:00", "Базы данных", "ауд. 304", "lecture"),
                        new LessonDto("10:15-11:45", "Web-разработка", "ауд. 208", "lab"))),
                new ScheduleDayDto("Вт", 28, Collections.singletonList(
                        new LessonDto("12:00-13:30", "Алгоритмы", "ауд. 101", "practice"))),
                new ScheduleDayDto("Ср", 29, Arrays.asList(
                        new LessonDto("8:30-10:00", "Операционные системы", "ауд. 402", "lecture"),
                        new LessonDto("10:15-11:45", "Английский язык", "ауд. 215", "seminar"),
                        new LessonDto("13:00-14:30", "Проектирование ПО", "ауд. 303", "lecture"))),
                new ScheduleDayDto("Чт", 30, Arrays.asList(
                        new LessonDto("10:15-11:45", "Базы данных", "ауд. 304", "lab"),
                        new LessonDto("15:00-16:30", "Физическая культура", "Спортзал", "practice"))),
                new ScheduleDayDto("Пт", 31, Arrays.asList(
                        new LessonDto("8:30-10:00", "Сети и телекоммуникации", "ауд. 501", "lecture"),
                        new LessonDto("10:15-11:45", "Математический анализ", "ауд. 108", "seminar"))),
                new ScheduleDayDto("Сб", 1, Collections.emptyList())
        );
    }

    @Override
    public List<DebtDto> loadDebts(String studentId) {
        return Arrays.asList(
                new DebtDto("debt-1", "Математический анализ", "Козлов Д.Р.", "Экзамен не сдан", "14 февраля 2025, 10:00", "ауд. 108"),
                new DebtDto("debt-2", "Учебная практика", "Смирнова О.П.", "Не загружен отчет", "22 февраля 2025, 14:00", "ауд. 106")
        );
    }
}
