package ru.university.studentapi.dto;

public class LessonDto {
    private String time;
    private String name;
    private String room;
    private String type;

    public LessonDto() {
    }

    public LessonDto(String time, String name, String room, String type) {
        this.time = time;
        this.name = name;
        this.room = room;
        this.type = type;
    }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
