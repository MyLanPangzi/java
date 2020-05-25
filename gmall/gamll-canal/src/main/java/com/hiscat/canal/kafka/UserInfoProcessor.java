package com.hiscat.canal.kafka;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.hiscat.constan.GmallConstants;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author hiscat
 */
@Component
public class UserInfoProcessor extends AbstractKafkaProcessor {

    @Override
    public String tableName() {
        return GmallConstants.USER_INFO_TABLE;
    }

    @Override
    public List<CanalEntry.EventType> eventTypes() {
        return Arrays.asList(CanalEntry.EventType.INSERT, CanalEntry.EventType.UPDATE);
    }

    @Override
    public List<CanalEntry.Column> columns(CanalEntry.RowData row) {
        return row.getAfterColumnsList();
    }

    @Override
    public String topic() {
        return GmallConstants.KAFKA_TOPIC_USER_INFO;
    }

}
