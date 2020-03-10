package com.hiscat.kafka.interceptor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Arrays;
import java.util.Properties;

/**
 * @author hiscat
 */
public class InterceptorTest {
    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop104:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, Arrays.asList("com.hiscat.kafka.interceptor.TimestampInterceptor",
                "com.hiscat.kafka.interceptor.CounterInterceptor"));
        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 3; i++) {
            producer.send(new ProducerRecord<String, String>("interceptor-topic", Integer.toString(i), Integer.toString(i)));
        }
        producer.close();
    }
}
