package ru.university.studentapi.event;

public interface GradeEventPublisher {
    void publish(GradeChangedEvent event);
}
