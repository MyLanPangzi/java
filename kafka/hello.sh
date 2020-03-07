#!/usr/bin/env bash
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test
kafka-topics.sh --list --bootstrap-server localhost:9092
kafka-console-producer.sh --broker-list localhost:9092 --topic test
kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test \
--consumer.config config/consumer.properties --from-beginning
kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 3 --partitions 1 --topic my-replicated-topic
kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic my-replicated-topic
kafka-console-producer.sh --broker-list localhost:9092 --topic my-replicated-topic
kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic my-replicated-topic
connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties
kafka-console-consumer.sh --topic __consumer_offsets \
--bootstrap-server localhost:9092 --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" \
--consumer.config config/consumer.properties --from-beginning
