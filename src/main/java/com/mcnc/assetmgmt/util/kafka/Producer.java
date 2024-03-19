package com.mcnc.assetmgmt.util.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private String topic;

    @Autowired
    public Producer(KafkaTemplate<String, Object> kafkaTemplate,
                    @Value("${kafka.topic}")String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void create(String message) {
        kafkaTemplate.send(topic, message);
    }
}
