package com.hiscat.gmalllogger.controller;

import com.alibaba.fastjson.JSONObject;
import com.hiscat.constan.GmallConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hiscat
 */
@Slf4j
@RestController
@AllArgsConstructor
public class LoggerController {

    public static final String STARTUP_EVENT = "startup";
    public static final String EVENT_TYPE = "type";
    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/log")
    public String log(String logString) {
        LOGGER.info(logString);
        JSONObject json = JSONObject.parseObject(logString);
        json.put("ts", System.currentTimeMillis());
        if (STARTUP_EVENT.equalsIgnoreCase(json.getString(EVENT_TYPE))) {
            kafkaTemplate.send(GmallConstants.KAFKA_TOPIC_STARTUP, json.toJSONString());
        } else {
            kafkaTemplate.send(GmallConstants.KAFKA_TOPIC_EVENT, json.toJSONString());
        }
        return "ok";
    }

}
