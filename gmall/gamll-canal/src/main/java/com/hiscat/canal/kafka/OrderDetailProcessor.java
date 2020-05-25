package com.hiscat.canal.kafka;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.hiscat.constan.GmallConstants;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author hiscat
 */
@Component
public class OrderDetailProcessor extends AbstractKafkaProcessor {

    @Override
    public String tableName() {
        return GmallConstants.ORDER_DETAIL_TABLE;
    }

    @Override
    public List<CanalEntry.EventType> eventTypes() {
        return Collections.singletonList(CanalEntry.EventType.INSERT);
    }

    @Override
    public List<CanalEntry.Column> columns(CanalEntry.RowData row) {
        return row.getAfterColumnsList();
    }

    @Override
    public String topic() {
        return GmallConstants.KAFKA_TOPIC_ORDER_DETAIL;
    }

}
