package ru.university.studentapi.dto;

public class GradeDto {
    private String id;
    private String subject;
    private String teacher;
    private String type;
    private String typeLabel;
    private String date;
    private String grade;
    private String status;

    public GradeDto() {
    }

    public GradeDto(String id, String subject, String teacher, String type, String typeLabel, String date, String grade, String status) {
        this.id = id;
        this.subject = subject;
        this.teacher = teacher;
        this.type = type;
        this.typeLabel = typeLabel;
        this.date = date;
        this.grade = grade;
        this.status = status;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTeacher() { return teacher; }
    public void setTeacher(String teacher) { this.teacher = teacher; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getTypeLabel() { return typeLabel; }
    public void setTypeLabel(String typeLabel) { this.typeLabel = typeLabel; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
