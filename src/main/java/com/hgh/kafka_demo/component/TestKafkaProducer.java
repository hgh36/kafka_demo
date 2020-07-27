package com.hgh.kafka_demo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TestKafkaProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void sendMsg(String msg){
        try {
            MyMessage myMessage = new MyMessage(UUID.randomUUID().toString(), msg);
            String json = objectMapper.writeValueAsString(myMessage);
            kafkaTemplate.send("test", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
