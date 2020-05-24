package com.hiscat.canal;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hiscat.constan.GmallConstants;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hiscat
 */
public class CanalKafkaClient {

    public static final String ORDER_INTO_TABLE = "order_info";

    public static void main(String[] args) throws InterruptedException {
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("hadoop102", 11111),
                "example",
                "", ""
        );
        canalConnector.connect();
        canalConnector.subscribe("gmall.*");
        canalConnector.rollback();

        int emptyCount = 0;
        KafkaProducer<String, String> kafkaProducer = getKafkaProducer();
        while (emptyCount < Integer.MAX_VALUE) {
            int batchSize = 1000;
            Message message = canalConnector.getWithoutAck(batchSize);
            if (message.getEntries().isEmpty()) {
                System.out.println("empty message");
                Thread.sleep(3000);
                emptyCount++;
                continue;
            }
            emptyCount = 0;
            message.getEntries()
                    .stream()
                    .filter(entry -> entry.getHeader().getTableName().equalsIgnoreCase(ORDER_INTO_TABLE))
                    .map(CanalKafkaClient::extractRowChange)
                    .filter(rowChange -> rowChange.getEventType().equals(CanalEntry.EventType.INSERT))
                    .flatMap(e -> e.getRowDatasList().stream())
                    .map(CanalKafkaClient::map2Json)
                    .forEach(json -> {
                        kafkaProducer.send(new ProducerRecord<>(GmallConstants.KAFKA_TOPIC_ORDER_INFO, json.toJSONString()));
                    });
        }

        kafkaProducer.close();
        canalConnector.unsubscribe();
        canalConnector.disconnect();
    }

    private static KafkaProducer<String, String> getKafkaProducer() {
        Map<String, Object> props = new HashMap<>(32);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092,hadoop104:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        return new KafkaProducer<>(props);
    }

    private static CanalEntry.RowChange extractRowChange(CanalEntry.Entry entry) {
        try {
            return CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException("parse error:" + e.getMessage());
        }
    }

    private static JSONObject map2Json(CanalEntry.RowData rowData) {
        JSONObject json = new JSONObject();
        rowData.getAfterColumnsList().forEach(column -> {
            json.put(column.getName(), column.getValue());
        });
        return json;
    }
}
