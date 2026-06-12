package ru.university.studentapi.dto;

import java.util.List;

public class ScheduleDayDto {
    private String day;
    private Integer date;
    private List<LessonDto> lessons;

    public ScheduleDayDto() {
    }

    public ScheduleDayDto(String day, Integer date, List<LessonDto> lessons) {
        this.day = day;
        this.date = date;
        this.lessons = lessons;
    }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public Integer getDate() { return date; }
    public void setDate(Integer date) { this.date = date; }
    public List<LessonDto> getLessons() { return lessons; }
    public void setLessons(List<LessonDto> lessons) { this.lessons = lessons; }
}
