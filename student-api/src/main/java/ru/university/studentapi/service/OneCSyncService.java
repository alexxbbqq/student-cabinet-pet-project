package ru.university.studentapi.service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.university.studentapi.dto.DebtDto;
import ru.university.studentapi.dto.GradeDto;
import ru.university.studentapi.dto.LessonDto;
import ru.university.studentapi.dto.ScheduleDayDto;
import ru.university.studentapi.dto.StudentProfileDto;
import ru.university.studentapi.entity.DebtEntity;
import ru.university.studentapi.entity.GradeEntity;
import ru.university.studentapi.entity.LessonEntity;
import ru.university.studentapi.entity.ScheduleDayEntity;
import ru.university.studentapi.entity.StudentEntity;
import ru.university.studentapi.integration.StudentDataProvider;
import ru.university.studentapi.integration.onec.OneCProperties;
import ru.university.studentapi.repository.DebtRepository;
import ru.university.studentapi.repository.GradeRepository;
import ru.university.studentapi.repository.ScheduleDayRepository;
import ru.university.studentapi.repository.StudentRepository;

@Service
@ConditionalOnProperty(name = "app.integration.onec.sync-enabled", havingValue = "true", matchIfMissing = true)
public class OneCSyncService {
    private static final Logger log = LoggerFactory.getLogger(OneCSyncService.class);
    private static final String DEFAULT_DEMO_STUDENT_ID = "2021-301-047";

    private final OneCProperties properties;
    private final StudentDataProvider studentDataProvider;
    private final StudentRepository studentRepository;
    private final GradeRepository gradeRepository;
    private final ScheduleDayRepository scheduleDayRepository;
    private final DebtRepository debtRepository;

    public OneCSyncService(
            OneCProperties properties,
            StudentDataProvider studentDataProvider,
            StudentRepository studentRepository,
            GradeRepository gradeRepository,
            ScheduleDayRepository scheduleDayRepository,
            DebtRepository debtRepository) {
        this.properties = properties;
        this.studentDataProvider = studentDataProvider;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.scheduleDayRepository = scheduleDayRepository;
        this.debtRepository = debtRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void syncOnStartup() {
        syncDemoStudents();
    }

    @Scheduled(
            initialDelayString = "${app.integration.onec.sync-fixed-delay-ms:600000}",
            fixedDelayString = "${app.integration.onec.sync-fixed-delay-ms:600000}")
    public void syncBySchedule() {
        syncDemoStudents();
    }

    public void syncDemoStudents() {
        for (String studentId : demoStudentIds()) {
            try {
                syncStudent(studentId);
            } catch (Exception ex) {
                log.warn("1C sync failed for student {}: {}", studentId, ex.getMessage());
            }
        }
    }

    public void syncStudent(String studentId) {
        StudentProfileDto profile = studentDataProvider.loadProfile(studentId);
        if (profile == null) {
            throw new IllegalStateException("1C returned no profile for student " + studentId);
        }
        StudentEntity student = studentRepository.findByOnecId(studentId)
                .orElseGet(() -> {
                    StudentEntity created = new StudentEntity();
                    created.setId(stableId("student", studentId));
                    created.setOnecId(studentId);
                    return created;
                });

        student.setFullName(profile.getFullName());
        student.setInitials(profile.getInitials());
        student.setGroupName(profile.getGroupName());
        student.setCourse(profile.getCourse());
        student.setEmail(profile.getEmail());
        student.setPhone(profile.getPhone());
        student.setBirthDate(profile.getBirthDate());
        student.setFaculty(profile.getFaculty());
        student.setDirection(profile.getDirection());
        student.setCurator(profile.getCurator());
        student.setYearIn(profile.getYearIn());
        student.setYearOut(profile.getYearOut());
        student.setRecordBook(profile.getRecordBook());
        student.setEducationForm(profile.getEducationForm());
        student = studentRepository.save(student);

        replaceGrades(student, studentDataProvider.loadGrades(studentId));
        replaceSchedule(student, studentDataProvider.loadSchedule(studentId));
        replaceDebts(student, studentDataProvider.loadDebts(studentId));
    }

    private List<String> demoStudentIds() {
        if (properties.getDemoStudentIds() == null || properties.getDemoStudentIds().isEmpty()) {
            return java.util.Collections.singletonList(DEFAULT_DEMO_STUDENT_ID);
        }
        return properties.getDemoStudentIds();
    }

    private void replaceGrades(StudentEntity student, List<GradeDto> grades) {
        gradeRepository.deleteByStudent(student);
        gradeRepository.flush();
        for (GradeDto dto : grades) {
            GradeEntity entity = new GradeEntity();
            entity.setId(stableId("grade", student.getOnecId(), dto.getId()));
            entity.setStudent(student);
            entity.setOnecId(dto.getId());
            entity.setSubject(dto.getSubject());
            entity.setTeacher(dto.getTeacher());
            entity.setType(dto.getType());
            entity.setTypeLabel(dto.getTypeLabel());
            entity.setGradeDateLabel(dto.getDate());
            entity.setGradeValue(dto.getGrade());
            entity.setStatus(dto.getStatus());
            gradeRepository.save(entity);
        }
    }

    private void replaceSchedule(StudentEntity student, List<ScheduleDayDto> schedule) {
        scheduleDayRepository.deleteByStudent(student);
        scheduleDayRepository.flush();
        for (int dayIndex = 0; dayIndex < schedule.size(); dayIndex++) {
            ScheduleDayDto dto = schedule.get(dayIndex);
            ScheduleDayEntity day = new ScheduleDayEntity();
            day.setId(stableId("schedule-day", student.getOnecId(), dto.getDay()));
            day.setStudent(student);
            day.setDayCode(dto.getDay());
            day.setDayNumber(dto.getDate());
            day.setSortOrder(dayIndex);

            List<LessonDto> lessons = dto.getLessons();
            for (int lessonIndex = 0; lessonIndex < lessons.size(); lessonIndex++) {
                LessonDto lessonDto = lessons.get(lessonIndex);
                LessonEntity lesson = new LessonEntity();
                lesson.setId(stableId("lesson", student.getOnecId(), dto.getDay(), String.valueOf(lessonIndex), lessonDto.getName()));
                lesson.setLessonTime(lessonDto.getTime());
                lesson.setName(lessonDto.getName());
                lesson.setRoom(lessonDto.getRoom());
                lesson.setType(lessonDto.getType());
                lesson.setSortOrder(lessonIndex);
                day.addLesson(lesson);
            }
            scheduleDayRepository.save(day);
        }
    }

    private void replaceDebts(StudentEntity student, List<DebtDto> debts) {
        debtRepository.deleteByStudent(student);
        debtRepository.flush();
        for (DebtDto dto : debts) {
            DebtEntity entity = new DebtEntity();
            entity.setId(stableId("debt", student.getOnecId(), dto.getId()));
            entity.setStudent(student);
            entity.setOnecId(dto.getId());
            entity.setSubject(dto.getSubject());
            entity.setTeacher(dto.getTeacher());
            entity.setReason(dto.getReason());
            entity.setRetakeDate(dto.getRetakeDate());
            entity.setLocation(dto.getLocation());
            debtRepository.save(entity);
        }
    }

    private UUID stableId(String... parts) {
        String value = String.join(":", parts);
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }
}
