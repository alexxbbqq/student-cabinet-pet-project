package ru.university.studentapi.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import ru.university.studentapi.event.GradeChangedEvent;
import ru.university.studentapi.event.GradeEventPublisher;
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
    private final GradeEventPublisher gradeEventPublisher;

    public OneCSyncService(
            OneCProperties properties,
            StudentDataProvider studentDataProvider,
            StudentRepository studentRepository,
            GradeRepository gradeRepository,
            ScheduleDayRepository scheduleDayRepository,
            DebtRepository debtRepository,
            GradeEventPublisher gradeEventPublisher) {
        this.properties = properties;
        this.studentDataProvider = studentDataProvider;
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.scheduleDayRepository = scheduleDayRepository;
        this.debtRepository = debtRepository;
        this.gradeEventPublisher = gradeEventPublisher;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void syncOnStartup() {
        syncDemoStudents();
    }

    @Scheduled(
            initialDelayString = "${app.integration.onec.sync-fixed-delay-ms:600000}",
            fixedDelayString = "${app.integration.onec.sync-fixed-delay-ms:600000}")
    @Transactional
    public void syncBySchedule() {
        syncDemoStudents();
    }

    @Transactional
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
        Map<String, GradeEntity> existingByOnecId = new HashMap<>();
        for (GradeEntity g : gradeRepository.findByStudentOrderBySubjectAsc(student)) {
            existingByOnecId.put(g.getOnecId(), g);
        }

        Set<String> incomingOnecIds = new HashSet<>();
        for (GradeDto dto : grades) {
            incomingOnecIds.add(dto.getId());
            GradeEntity entity = existingByOnecId.get(dto.getId());
            boolean isNew = entity == null;
            if (isNew) {
                entity = new GradeEntity();
                entity.setId(stableId("grade", student.getOnecId(), dto.getId()));
                entity.setStudent(student);
                entity.setOnecId(dto.getId());
            }

            String oldGrade = entity.getGradeValue();
            String oldStatus = entity.getStatus();

            entity.setSubject(dto.getSubject());
            entity.setTeacher(dto.getTeacher());
            entity.setType(dto.getType());
            entity.setTypeLabel(dto.getTypeLabel());
            entity.setGradeDateLabel(dto.getDate());
            entity.setGradeValue(dto.getGrade());
            entity.setStatus(dto.getStatus());
            gradeRepository.save(entity);

            boolean valueChanged = isNew
                    ? dto.getGrade() != null
                    : !Objects.equals(oldGrade, dto.getGrade()) || !Objects.equals(oldStatus, dto.getStatus());
            if (valueChanged) {
                gradeEventPublisher.publish(new GradeChangedEvent(
                        student.getOnecId(),
                        dto.getId(),
                        dto.getSubject(),
                        oldGrade,
                        dto.getGrade(),
                        oldStatus,
                        dto.getStatus(),
                        Instant.now()));
            }
        }

        for (Map.Entry<String, GradeEntity> entry : existingByOnecId.entrySet()) {
            if (!incomingOnecIds.contains(entry.getKey())) {
                gradeRepository.delete(entry.getValue());
            }
        }
    }

    private void replaceSchedule(StudentEntity student, List<ScheduleDayDto> schedule) {
        Map<String, ScheduleDayEntity> existingByDayCode = new HashMap<>();
        for (ScheduleDayEntity d : scheduleDayRepository.findByStudentOrderBySortOrderAsc(student)) {
            existingByDayCode.put(d.getDayCode(), d);
        }

        Set<String> incomingDayCodes = new HashSet<>();
        for (int dayIndex = 0; dayIndex < schedule.size(); dayIndex++) {
            ScheduleDayDto dto = schedule.get(dayIndex);
            incomingDayCodes.add(dto.getDay());

            ScheduleDayEntity day = existingByDayCode.get(dto.getDay());
            if (day == null) {
                day = new ScheduleDayEntity();
                day.setId(stableId("schedule-day", student.getOnecId(), dto.getDay()));
                day.setStudent(student);
                day.setDayCode(dto.getDay());
            }
            day.setDayNumber(dto.getDate());
            day.setSortOrder(dayIndex);

            day.getLessons().clear();
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

        for (Map.Entry<String, ScheduleDayEntity> entry : existingByDayCode.entrySet()) {
            if (!incomingDayCodes.contains(entry.getKey())) {
                scheduleDayRepository.delete(entry.getValue());
            }
        }
    }

    private void replaceDebts(StudentEntity student, List<DebtDto> debts) {
        Map<String, DebtEntity> existingByOnecId = new HashMap<>();
        for (DebtEntity d : debtRepository.findByStudentOrderBySubjectAsc(student)) {
            existingByOnecId.put(d.getOnecId(), d);
        }

        Set<String> incomingOnecIds = new HashSet<>();
        for (DebtDto dto : debts) {
            incomingOnecIds.add(dto.getId());
            DebtEntity entity = existingByOnecId.get(dto.getId());
            if (entity == null) {
                entity = new DebtEntity();
                entity.setId(stableId("debt", student.getOnecId(), dto.getId()));
                entity.setStudent(student);
                entity.setOnecId(dto.getId());
            }
            entity.setSubject(dto.getSubject());
            entity.setTeacher(dto.getTeacher());
            entity.setReason(dto.getReason());
            entity.setRetakeDate(dto.getRetakeDate());
            entity.setLocation(dto.getLocation());
            debtRepository.save(entity);
        }

        for (Map.Entry<String, DebtEntity> entry : existingByOnecId.entrySet()) {
            if (!incomingOnecIds.contains(entry.getKey())) {
                debtRepository.delete(entry.getValue());
            }
        }
    }

    private UUID stableId(String... parts) {
        String value = String.join(":", parts);
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }
}
