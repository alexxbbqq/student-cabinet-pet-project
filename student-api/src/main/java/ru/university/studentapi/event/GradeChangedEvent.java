package ru.university.studentapi.event;

import java.time.Instant;

public class GradeChangedEvent {
    private String studentOnecId;
    private String gradeOnecId;
    private String subject;
    private String oldValue;
    private String newValue;
    private String oldStatus;
    private String newStatus;
    private Instant changedAt;

    public GradeChangedEvent() {
    }

    public GradeChangedEvent(String studentOnecId, String gradeOnecId, String subject,
            String oldValue, String newValue, String oldStatus, String newStatus, Instant changedAt) {
        this.studentOnecId = studentOnecId;
        this.gradeOnecId = gradeOnecId;
        this.subject = subject;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

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
    public Instant getChangedAt() { return changedAt; }
    public void setChangedAt(Instant changedAt) { this.changedAt = changedAt; }
}
