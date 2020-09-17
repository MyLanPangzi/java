kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092
kafka-topics.sh --describe --topic quickstart-events --bootstrap-server localhost:9092
kafka-topics.sh --list  --bootstrap-server localhost:9092
kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092
kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092

kafka-producer-perf-test.sh  --topic test --record-size 100 --num-records 100000 \
--throughput -1 --producer-props bootstrap.servers=hadoop102:9092,hadoop103:9092,hadoop104:9092
kafka-consumer-perf-test.sh --broker-list hadoop102:9092,hadoop103:9092,hadoop104:9092 \
--topic test --fetch-size 10000 --messages 10000000 --threads 1

kafka-console-consumer.sh --topic topic_log --from-beginning --bootstrap-server localhost:9092
kafka-console-consumer.sh --topic topic_log  --bootstrap-server localhost:9092
kafka-topics.sh --describe --topic topic_log --bootstrap-server localhost:9092
