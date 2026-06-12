package ru.university.studentapi.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class StudentEntity {
    @Id
    private UUID id;
    @Column(name = "onec_id", nullable = false, unique = true)
    private String onecId;
    @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String initials;
    @Column(name = "group_name", nullable = false)
    private String groupName;
    @Column(nullable = false)
    private Integer course;
    private String email;
    private String phone;
    @Column(name = "birth_date")
    private String birthDate;
    private String faculty;
    private String direction;
    private String curator;
    @Column(name = "year_in")
    private Integer yearIn;
    @Column(name = "year_out")
    private Integer yearOut;
    @Column(name = "record_book")
    private String recordBook;
    @Column(name = "education_form")
    private String educationForm;
    @Column(name = "synced_at", nullable = false)
    private LocalDateTime syncedAt;

    @PrePersist
    @PreUpdate
    void markSynced() {
        syncedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getOnecId() { return onecId; }
    public void setOnecId(String onecId) { this.onecId = onecId; }
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
    public LocalDateTime getSyncedAt() { return syncedAt; }
    public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }
}
