package ru.university.studentapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "grades")
public class GradeEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private StudentEntity student;
    @Column(name = "onec_id")
    private String onecId;
    private String subject;
    private String teacher;
    private String type;
    @Column(name = "type_label")
    private String typeLabel;
    @Column(name = "grade_date_label")
    private String gradeDateLabel;
    @Column(name = "grade_value")
    private String gradeValue;
    private String status;
    @Column(name = "synced_at")
    private LocalDateTime syncedAt;

    @PrePersist
    @PreUpdate
    void markSynced() {
        syncedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public StudentEntity getStudent() { return student; }
    public void setStudent(StudentEntity student) { this.student = student; }
    public String getOnecId() { return onecId; }
    public void setOnecId(String onecId) { this.onecId = onecId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTypeLabel() { return typeLabel; }
    public void setTypeLabel(String typeLabel) { this.typeLabel = typeLabel; }
    public String getGradeDateLabel() { return gradeDateLabel; }
    public void setGradeDateLabel(String gradeDateLabel) { this.gradeDateLabel = gradeDateLabel; }
    public String getGradeValue() { return gradeValue; }
    public void setGradeValue(String gradeValue) { this.gradeValue = gradeValue; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getSyncedAt() { return syncedAt; }
    public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }
}
