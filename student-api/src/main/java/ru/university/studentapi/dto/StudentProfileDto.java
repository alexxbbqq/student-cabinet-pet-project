package ru.university.studentapi.dto;

public class StudentProfileDto {
    private String fullName;
    private String initials;
    private String groupName;
    private Integer course;
    private String email;
    private String phone;
    private String birthDate;
    private String faculty;
    private String direction;
    private String curator;
    private Integer yearIn;
    private Integer yearOut;
    private String recordBook;
    private String educationForm;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getInitials() { return initials; }
    public void setInitials(String initials) { this.initials = initials; }
    public String getGroupName() { return groupName; }
    public void setGroupName(String groupName) { this.groupName = groupName; }
    public Integer getCourse() { return course; }
    public void setCourse(Integer course) { this.course = course; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public String getCurator() { return curator; }
    public void setCurator(String curator) { this.curator = curator; }
    public Integer getYearIn() { return yearIn; }
    public void setYearIn(Integer yearIn) { this.yearIn = yearIn; }
    public Integer getYearOut() { return yearOut; }
    public void setYearOut(Integer yearOut) { this.yearOut = yearOut; }
    public String getRecordBook() { return recordBook; }
    public void setRecordBook(String recordBook) { this.recordBook = recordBook; }
    public String getEducationForm() { return educationForm; }
    public void setEducationForm(String educationForm) { this.educationForm = educationForm; }
}
