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
@Table(name = "debts")
public class DebtEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private StudentEntity student;
    @Column(name = "onec_id")
    private String onecId;
    private String subject;
    private String teacher;
    private String reason;
    @Column(name = "retake_date")
    private String retakeDate;
    private String location;
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
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getRetakeDate() { return retakeDate; }
    public void setRetakeDate(String retakeDate) { this.retakeDate = retakeDate; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getSyncedAt() { return syncedAt; }
    public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }
}
