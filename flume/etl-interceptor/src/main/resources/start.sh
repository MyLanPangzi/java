nohup flume-ng agent -n a1 -c conf -f conf/file_flume_kafka.conf \
-Dflume.root.logger=INFO,LOGFILE >/dev/null 2>&1 &

ps -ef|grep file_flume_kafka|grep -v grep | awk '{print \$2}'|xargs kill -9

