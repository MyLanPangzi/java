package com.hiscat.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
class KafkaTest {

    @Test
    void testProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop104:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
//        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "com.hiscat.kafka.partitioner.EvenOddPartitioner");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for (int i = 0; i < 5; i++) {
//            producer.send(new ProducerRecord<String, String>("my-topic",  Integer.toString(i)));
//            producer.send(new ProducerRecord<String, String>("my-topic", Integer.toString(i), Integer.toString(i)));
//            producer.send(new ProducerRecord<String, String>("my-topic", 0, Integer.toString(i), Integer.toString(i)));
//            producer.send(new ProducerRecord<String, String>("my-topic", 0, Integer.toString(i), Integer.toString(i))).get();
            producer.send(new ProducerRecord<>("my-topic", Integer.toString(i)), (metadata, exception) -> LOGGER.info("topic:{},partition:{},offset:{},timestamp:{}",
                    metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp()));
        }

//        TimeUnit.SECONDS.sleep(3);
        producer.close();
    }

    @Test
    void testConsumer() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "hadoop104:9092");
        props.setProperty("group.id", "test");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("hello", "hiscat"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                LOGGER.info("topic:{},partition:{},offset:{},timestamp:{},timestampType:{},key:{},val:{}",
                        record.topic(), record.partition(), record.offset(),
                        record.timestamp(), record.timestampType(), record.key(), record.value());
            }
        }
    }

    @Test
    void testManualCommit() {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "hadoop104:9092");
        props.setProperty("group.id", "test");
//        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("my-topic"));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                LOGGER.info("topic:{},partition:{},offset:{},timestamp:{},timestampType:{},key:{},val:{}",
                        record.topic(), record.partition(), record.offset(),
                        record.timestamp(), record.timestampType(), record.key(), record.value());
            }
            consumer.commitAsync((offsets, exception) -> {
            });
//                        consumer.commitSync();
        }
    }
}