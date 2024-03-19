package com.mcnc.assetmgmt.util.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;
/**
 * title : KafkaProducerConfig
 *
 * description : Kafka 기본설정
 *
 * reference :
 *       class com.fasterxml.jackson.databind.ser.std.StringSerializer is not an instance of org.apache.kafka.common.serialization.Serializer
 *       해당 오류 => https://stackoverflow.com/questions/53801565/common-kafkaexception-com-fasterxml-jackson-databind-ser-std-stringserializer-i
 *
 *
 *
 * author : 임현영
 * date : 2024.03.06
 **/
@Configuration
public class KafkaConfig {
    private String KAFKA_SERVER_IP;

    public KafkaConfig(@Value("${kafka.server.ip}") String KAFKA_SERVER_IP){
        this.KAFKA_SERVER_IP = KAFKA_SERVER_IP;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_IP);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}