package ru.university.studentapi.dto;

import java.time.LocalDateTime;

public class GradeHistoryEntryDto {
    private String subject;
    private String oldValue;
    private String newValue;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt;

    public GradeHistoryEntryDto() {
    }

    public GradeHistoryEntryDto(String subject, String oldValue, String newValue,
            String oldStatus, String newStatus, LocalDateTime changedAt) {
        this.subject = subject;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
    }

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
