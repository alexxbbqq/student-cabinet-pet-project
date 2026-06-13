package ru.university.studentapi.event;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpGradeEventPublisher implements GradeEventPublisher {
    @Override
    public void publish(GradeChangedEvent event) {
        // Kafka disabled (app.kafka.enabled=false) - grade change events are not published.
    }
}
