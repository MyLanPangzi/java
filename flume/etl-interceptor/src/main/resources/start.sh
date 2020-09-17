nohup flume-ng agent -n a1 -c conf -f /opt/module/flume-1.9.0/conf/file_flume_kafka.conf -Dflume.root.logger=INFO,LOGFILE >/dev/null 2>&1 &

ps -ef|grep file_flume_kafka|grep -v grep | awk '{print \$2}'|xargs kill -9

nohup flume-ng agent -n a1 -c conf -f /opt/module/flume-1.9.0/conf/kafka_file_hdfs.conf -Dflume.root.logger=INFO,LOGFILE >/dev/null 2>&1 &

ps -ef|grep kafka_file_hdfs|grep -v grep | awk '{print \$2}'|xargs kill -9
