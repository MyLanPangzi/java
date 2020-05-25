package com.hiscat.canal.kafka;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

/**
 * @author hiscat
 */
@Slf4j
public abstract class AbstractKafkaProcessor {

    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 声明所需处理的表名
     *
     * @return 表名
     */
    public abstract String tableName();

    /**
     * 声明要处理的事件类型
     *
     * @return 要处理的事件类型
     */
    public abstract List<CanalEntry.EventType> eventTypes();

    /**
     * 处理日志条目
     *
     * @param row 日志条目
     * @return json
     */
    public abstract List<CanalEntry.Column> columns(CanalEntry.RowData row);

    /**
     * 声明要写入的Kafka主题
     *
     * @return kafka主题
     */
    public abstract String topic();

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    public void setKafkaTemplate(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void process(List<CanalEntry.Entry> entries) {
        // rowChanges -> columns -> jsons -> kafka
        entries
                .stream()
                .map(this::entry2RowChange)
                .filter(e -> eventTypes().contains(e.getEventType()))
                .flatMap(e -> e.getRowDatasList().stream())
                .map(this::columns)
                .map(this::columns2Json)
                .forEach(data -> {
                    LOGGER.info("write Kafka table:{},event types:{},topic:{},data:{}",
                            this.tableName(), eventTypes(), this.topic(), data);
//                    try {
//                        TimeUnit.SECONDS.sleep(new Random().nextInt(3));
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    kafkaTemplate.send(this.topic(), data);
                });
    }

    protected CanalEntry.RowChange entry2RowChange(CanalEntry.Entry entry) {
        try {
            return CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("parse error:" + e.getMessage());
        }
    }

    protected String columns2Json(List<CanalEntry.Column> columns) {
        JSONObject json = new JSONObject();
        columns.forEach(column -> {
            json.put(column.getName(), column.getValue());
        });
        return json.toJSONString();
    }

}
