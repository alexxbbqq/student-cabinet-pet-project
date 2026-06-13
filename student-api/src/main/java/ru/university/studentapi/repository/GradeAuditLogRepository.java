package ru.university.studentapi.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.university.studentapi.entity.GradeAuditLogEntity;

public interface GradeAuditLogRepository extends JpaRepository<GradeAuditLogEntity, UUID> {
    List<GradeAuditLogEntity> findByStudentOnecIdOrderByChangedAtDesc(String studentOnecId);
}
