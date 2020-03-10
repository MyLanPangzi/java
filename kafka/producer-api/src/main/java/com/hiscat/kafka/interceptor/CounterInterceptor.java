package com.hiscat.kafka.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hiscat
 */
@Slf4j
public class CounterInterceptor implements ProducerInterceptor<String, String> {
    private AtomicInteger successCounter = new AtomicInteger(0);
    private AtomicInteger failedCounter = new AtomicInteger(0);

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            successCounter.incrementAndGet();
        } else {
            failedCounter.incrementAndGet();
        }
    }

    @Override
    public void close() {
        LOGGER.info("success:{}", successCounter.get());
        LOGGER.info("failed:{}", failedCounter.get());
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
