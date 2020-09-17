hadoop jar /opt/module/hadoop-3.1.3/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-3.1.3-tests.jar \
TestDFSIO -write -nrFiles 3 -fileSize 128MB

hadoop jar /opt/module/hadoop-3.1.3/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-3.1.3-tests.jar \
TestDFSIO -read -nrFiles 3 -fileSize 128MB

hadoop jar /opt/module/hadoop-3.1.3/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount \
-Dmapreduce.job.inputformat.class=com.hadoop.mapreduce.LzoTextInputFormat /lzo /lzo-output

hadoop jar /opt/module/hadoop-3.1.3/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount \
-Dmapreduce.output.fileoutputformat.compress=true \
-Dmapreduce.output.fileoutputformat.compress.codec=com.hadoop.compression.lzo.LzopCodec \
/input /output

hdfs dfs -mkdir /lzo
hdfs dfs -put bigtable.lzo /lzo
hdfs dfs -ls -h /lzo
hdfs dfs -ls -h /lzo-output
#hdfs dfs -cat /lzo-output/part-r-00000
hdfs dfs -rm -r -f /lzo-output
hadoop jar /opt/module/hadoop-3.1.3/share/hadoop/common/hadoop-lzo-0.4.20.jar  \
com.hadoop.compression.lzo.DistributedLzoIndexer /lzo/bigtable.lzo
hadoop jar /opt/module/hadoop-3.1.3/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.1.3.jar wordcount \
-Dmapreduce.job.inputformat.class=com.hadoop.mapreduce.LzoTextInputFormat /lzo /lzo-output

