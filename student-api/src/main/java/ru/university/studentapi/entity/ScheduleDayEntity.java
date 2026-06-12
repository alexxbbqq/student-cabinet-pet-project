package ru.university.studentapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(name = "schedule_days")
public class ScheduleDayEntity {
    @Id
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id")
    private StudentEntity student;
    @Column(name = "day_code")
    private String dayCode;
    @Column(name = "day_number")
    private Integer dayNumber;
    @Column(name = "sort_order")
    private Integer sortOrder;
    @Column(name = "synced_at")
    private LocalDateTime syncedAt;
    @OneToMany(mappedBy = "scheduleDay", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder asc")
    private List<LessonEntity> lessons = new ArrayList<>();

    @PrePersist
    @PreUpdate
    void markSynced() {
        syncedAt = LocalDateTime.now();
    }

    public void addLesson(LessonEntity lesson) {
        lesson.setScheduleDay(this);
        lessons.add(lesson);
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public StudentEntity getStudent() { return student; }
    public void setStudent(StudentEntity student) { this.student = student; }
    public String getDayCode() { return dayCode; }
    public void setDayCode(String dayCode) { this.dayCode = dayCode; }
    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public LocalDateTime getSyncedAt() { return syncedAt; }
    public void setSyncedAt(LocalDateTime syncedAt) { this.syncedAt = syncedAt; }
    public List<LessonEntity> getLessons() { return lessons; }
    public void setLessons(List<LessonEntity> lessons) { this.lessons = lessons; }
}
