package com.hgh.kafka_demo.controller;

import com.hgh.kafka_demo.component.MyMessage;
import com.hgh.kafka_demo.component.TestKafkaProducer;
import com.hgh.kafka_demo.component.TestKafkaProducerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class TestProducerController {

    @Autowired
    private TestKafkaProducer testKafkaProducer;

    @Autowired
    private TestKafkaProducerClient testKafkaProducerClient;

    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg){
        testKafkaProducer.sendMsg(msg);
    }

    @GetMapping("sendMsg2/{msg}")
    public void sendMsg2(@PathVariable String msg){
        MyMessage message = new MyMessage(UUID.randomUUID().toString(), msg);
        testKafkaProducerClient.sendMsg("test", message);
    }
}
