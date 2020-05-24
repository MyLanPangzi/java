package com.hiscat.canal.kafka;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.Message;
import com.hiscat.canal.config.CanalProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

/**
 * @author hiscat
 */
@Component
@Slf4j
public class CanalKafkaClient {
    private CanalProperties canalProperties;
    private CanalConnector canalConnector;
    private Map<String, AbstractKafkaProcessor> processors;

    @PostConstruct
    public void init() {
        LOGGER.info("CanalKafkaClient initializing ...");
        canalConnector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalProperties.getHost(), canalProperties.getPot()),
                canalProperties.getDestination(),
                canalProperties.getUsername(), canalProperties.getPassword()
        );
        canalConnector.connect();
        canalConnector.rollback();
        canalConnector.subscribe(canalProperties.getDatabase() + ".*");

        Executors.newSingleThreadExecutor().submit(this::process);
        LOGGER.info("CanalKafkaClient initialized ...");

    }

    private void process() {
        try {
            //noinspection InfiniteLoopStatement
            while (true) {
                Message message = canalConnector.getWithoutAck(canalProperties.getBatchSize());
                if (message.getEntries().isEmpty()) {
                    LOGGER.info("empty message");
                    //noinspection BusyWait
                    Thread.sleep(canalProperties.getMaxIdleTime());
                    continue;
                }
                //message -> entries -> (table,entries)
                message.getEntries()
                        .stream()
                        .filter(entry -> processors.containsKey(entry.getHeader().getTableName()))
                        .collect(groupingBy(entry -> entry.getHeader().getTableName()))
                        .forEach((table, entries) -> processors.get(table).process(entries));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Autowired
    public void setCanalProperties(CanalProperties canalProperties) {
        this.canalProperties = canalProperties;
    }

    @Autowired(required = false)
    public void setProcessors(List<AbstractKafkaProcessor> processors) {
        this.processors = processors.stream().collect(toMap(AbstractKafkaProcessor::tableName, e -> e));
    }

    @PreDestroy
    public void destroy() {
        canalConnector.unsubscribe();
        canalConnector.disconnect();
    }
}
