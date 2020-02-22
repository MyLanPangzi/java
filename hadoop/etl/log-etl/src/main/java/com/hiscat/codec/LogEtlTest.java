package com.hiscat.codec;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author hiscat
 */
public class LogEtlTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\etl\\log-etl\\src\\main\\resources\\web.log"));
        job.setMapperClass(LogMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\etl\\log-etl\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setNumReduceTasks(0);

        job.setJarByClass(LogEtlTest.class);
        job.waitForCompletion(true);

    }

    @Slf4j
    private static class LogMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        LogBean log = new LogBean();
        Text k = new Text();


        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            parseLogBean(value);
            if (!this.log.getValid()) {
                return;
            }
            k.set(log.toString());
            context.write(k, NullWritable.get());
        }

        private void parseLogBean(Text value) {
            final String[] split = value.toString().split(" ");
            if (split.length < 11) {
                log.setValid(false);
                return;
            }
            log.setRemoteAddr(split[0])
                    .setRemoteUser(split[1])
                    .setTimeLocal(split[3].substring(1))
                    .setRequest(split[6])
                    .setStatus(split[8])
                    .setBodyBytesSent(split[9])
                    .setHttpReferer(split[10]);
            StringBuilder ua = new StringBuilder();
            for (int i = 11; i < split.length; i++) {
                ua.append(" ").append(split[i]);
            }
            log.setHttpUserAgent(ua.toString());
            log.setValid(log.getStatus().compareTo("400") < 0);
        }
    }

    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    private static class LogBean {
        private String remoteAddr;
        private String remoteUser;
        private String timeLocal;
        private String request;
        private String status;
        private String bodyBytesSent;
        private String httpReferer;
        private String httpUserAgent;
        private Boolean valid;

        @Override
        public String toString() {
            return valid +
                    "\t" + remoteAddr +
                    "\t" + remoteUser +
                    "\t" + timeLocal +
                    "\t" + request +
                    "\t" + status +
                    "\t" + bodyBytesSent +
                    "\t" + httpReferer +
                    "\t" + httpUserAgent;
        }
    }
}
