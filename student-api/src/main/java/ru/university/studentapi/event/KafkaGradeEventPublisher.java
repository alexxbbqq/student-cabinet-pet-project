package ru.university.studentapi.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaGradeEventPublisher implements GradeEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(KafkaGradeEventPublisher.class);

    private final KafkaTemplate<String, GradeChangedEvent> kafkaTemplate;
    private final String topic;

    public KafkaGradeEventPublisher(
            KafkaTemplate<String, GradeChangedEvent> kafkaTemplate,
            @org.springframework.beans.factory.annotation.Value("${app.kafka.grade-events-topic:grade-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void publish(GradeChangedEvent event) {
        kafkaTemplate.send(topic, event.getStudentOnecId(), event)
                .addCallback((ListenableFutureCallback<SendResult<String, GradeChangedEvent>>) new ListenableFutureCallback<SendResult<String, GradeChangedEvent>>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        log.warn("Failed to publish grade-changed event for student {}: {}",
                                event.getStudentOnecId(), ex.getMessage());
                    }

                    @Override
                    public void onSuccess(SendResult<String, GradeChangedEvent> result) {
                        // no-op
                    }
                });
    }
}
