#!/usr/bin/env bash
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic topic1.DLT.DLT
kafka-topics.sh --list --bootstrap-server localhost:9092
kafka-topics.sh --zookeeper hadoop102:2181 --list
kafka-topics.sh --zookeeper hadoop102:2181 --describe --topic topic_start
kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic hello
kafka-topics.sh --alter --bootstrap-server localhost:9092 --topic my-topic --partitions 3

kafka-console-producer.sh --broker-list localhost:9092 --topic my-topic

kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_start --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic_event --from-beginning
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic \
--consumer.config config/consumer.properties \
--from-beginning

kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 3 --partitions 1 --topic my-replicated-topic
kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic my-topic
kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic
connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
kafka-console-consumer.sh --topic __consumer_offsets \
--bootstrap-server localhost:9092 --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" \
--consumer.config config/consumer.properties --from-beginning

kafka-topics.sh --zookeeper hadoop102:2181,hadoop103:2181,hadoop104:2181  --create --replication-factor 1 --partitions 1 --topic topic_start