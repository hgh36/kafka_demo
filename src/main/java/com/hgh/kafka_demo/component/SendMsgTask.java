package com.hgh.kafka_demo.component;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.concurrent.BlockingQueue;

public class SendMsgTask implements Runnable {

    private BlockingQueue<KafkaProducer<String, String>> queue;

    private String topic;

    private String msg;

    public SendMsgTask(BlockingQueue<KafkaProducer<String, String>> queue, String topic, String msg) {
        this.queue = queue;
        this.topic = topic;
        this.msg = msg;
    }

    @Override
    public void run() {
        KafkaProducer<String, String> producer = null;
        try {
            producer = queue.take();
            ProducerRecord record = new ProducerRecord(topic, msg);
            producer.send(record);
        } catch (InterruptedException e){
            e.printStackTrace();
        } finally {
            if(producer != null) {
                try {//将producer归还到资源池
                    this.queue.put(producer);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
