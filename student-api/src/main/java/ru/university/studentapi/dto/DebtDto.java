package ru.university.studentapi.dto;

public class DebtDto {
    private String id;
    private String subject;
    private String teacher;
    private String reason;
    private String retakeDate;
    private String location;

    public DebtDto() {
    }

    public DebtDto(String id, String subject, String teacher, String reason, String retakeDate, String location) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.reason = reason;
        this.retakeDate = retakeDate;
        this.location = location;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
}
