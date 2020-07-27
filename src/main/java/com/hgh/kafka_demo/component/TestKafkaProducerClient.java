package com.hgh.kafka_demo.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.concurrent.*;

@Component
public class TestKafkaProducerClient {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.producer.retries}")
    private String retries;
    @Value("${spring.kafka.producer.batch-size}")
    private String batchSize;
    @Value("${spring.kafka.producer.buffer-memory}")
    private String bufferMemory;
    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;
    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;
    @Value("${spring.kafka.producer.acks}")
    private String acks;
    @Value("${spring.kafka.producer.compression-type}")
    private String compressionType;
    @Value("${spring.kafka.producerNum}")
    private int producerNum;

    //生产者集合
    private static BlockingQueue<KafkaProducer<String, String>> queue;

    private static ExecutorService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    static{
        service = new ThreadPoolExecutor(10, 15, 0L, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024));
    }

    @PostConstruct
    public void init(){
        Properties prop = new Properties();
        //配置broker地址，如果有多个在配置中用,隔开
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        //失败重试次数
        prop.put(ProducerConfig.RETRIES_CONFIG, retries);
        //批量发送大小
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        //缓存发送内存大小
        prop.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        //key的序列化
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        //value的序列化
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        //定义写入成功方式
        prop.put(ProducerConfig.ACKS_CONFIG, acks);
        //消息压缩方式
        prop.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, compressionType);

        queue = new LinkedBlockingQueue<>(producerNum);

        for(int i = 0; i < producerNum; i++) {
            KafkaProducer<String, String> producer = new KafkaProducer(prop);
            queue.add(producer);
        }
    }

    public void sendMsg(String topic, Object obj) {
        try {
            System.out.println("topic:"+topic+" .object:"+objectMapper.writeValueAsString(obj));
            service.execute(new SendMsgTask(this.queue, topic, objectMapper.writeValueAsString(obj)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
