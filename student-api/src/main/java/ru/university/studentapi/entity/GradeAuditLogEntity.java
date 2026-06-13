package ru.university.studentapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grade_audit_log")
public class GradeAuditLogEntity {
    @Id
    private UUID id;
    @Column(name = "student_onec_id")
    private String studentOnecId;
    @Column(name = "grade_onec_id")
    private String gradeOnecId;
    private String subject;
    @Column(name = "old_value")
    private String oldValue;
    @Column(name = "new_value")
    private String newValue;
    @Column(name = "old_status")
    private String oldStatus;
    @Column(name = "new_status")
    private String newStatus;
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getStudentOnecId() { return studentOnecId; }
    public void setStudentOnecId(String studentOnecId) { this.studentOnecId = studentOnecId; }
    public String getGradeOnecId() { return gradeOnecId; }
    public void setGradeOnecId(String gradeOnecId) { this.gradeOnecId = gradeOnecId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getOldValue() { return oldValue; }
    public void setOldValue(String oldValue) { this.oldValue = oldValue; }
    public String getNewValue() { return newValue; }
    public void setNewValue(String newValue) { this.newValue = newValue; }
    public String getOldStatus() { return oldStatus; }
    public void setOldStatus(String oldStatus) { this.oldStatus = oldStatus; }
    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }
    public LocalDateTime getChangedAt() { return changedAt; }
    public void setChangedAt(LocalDateTime changedAt) { this.changedAt = changedAt; }
}
