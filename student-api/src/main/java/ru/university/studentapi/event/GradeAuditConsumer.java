package ru.university.studentapi.event;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.university.studentapi.entity.GradeAuditLogEntity;
import ru.university.studentapi.repository.GradeAuditLogRepository;

@Component
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class GradeAuditConsumer {
    private static final Logger log = LoggerFactory.getLogger(GradeAuditConsumer.class);

    private final GradeAuditLogRepository gradeAuditLogRepository;

    public GradeAuditConsumer(GradeAuditLogRepository gradeAuditLogRepository) {
        this.gradeAuditLogRepository = gradeAuditLogRepository;
    }

    @KafkaListener(topics = "${app.kafka.grade-events-topic:grade-events}", groupId = "student-api-grade-audit")
    @Transactional
    public void onGradeChanged(GradeChangedEvent event) {
        GradeAuditLogEntity entity = new GradeAuditLogEntity();
        entity.setId(UUID.randomUUID());
        entity.setStudentOnecId(event.getStudentOnecId());
        entity.setGradeOnecId(event.getGradeOnecId());
        entity.setSubject(event.getSubject());
        entity.setOldValue(event.getOldValue());
        entity.setNewValue(event.getNewValue());
        entity.setOldStatus(event.getOldStatus());
        entity.setNewStatus(event.getNewStatus());
        entity.setChangedAt(LocalDateTime.ofInstant(event.getChangedAt(), ZoneOffset.UTC));
        gradeAuditLogRepository.save(entity);
        log.info("Recorded grade change for student {} ({}): {} -> {}",
                event.getStudentOnecId(), event.getSubject(), event.getOldValue(), event.getNewValue());
    }
}
