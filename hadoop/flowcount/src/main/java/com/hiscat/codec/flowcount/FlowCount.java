package com.hiscat.codec.flowcount;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.MRJobConfig;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author hiscat
 */
@Slf4j
public class FlowCount {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();
        job.getConfiguration().set(MRJobConfig.QUEUE_NAME, "hive");

        //in
        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setMapperClass(FlowCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setReducerClass(FlowCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //run
        job.setJarByClass(FlowCount.class);
        job.waitForCompletion(true);
    }

    @Data
    private static class FlowBean implements Writable {

        private long upFlow;
        private long downFlow;
        private long sumFlow;

        public FlowBean() {
        }

        public FlowBean(long upFlow, long downFlow) {
            this.upFlow = upFlow;
            this.downFlow = downFlow;
            this.sumFlow = upFlow + downFlow;
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeLong(upFlow);
            out.writeLong(downFlow);
            out.writeLong(sumFlow);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            this.upFlow = in.readLong();
            this.downFlow = in.readLong();
            this.sumFlow = in.readLong();
        }

        @Override
        public String toString() {
            return String.format("%s\t%s\t%s\t", upFlow, downFlow, sumFlow);
        }
    }

    private static class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

        Text outKey = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");

            outKey.set(split[1]);
            final long upFlow = Long.parseLong(split[split.length - 3]);
            final long downFlow = Long.parseLong(split[split.length - 2]);
            context.write(outKey, new FlowBean(upFlow, downFlow));
        }
    }

    private static class FlowCountReducer extends Reducer<Text, FlowBean, Text, FlowBean> {


        @Override
        protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
            long sumUp = 0;
            long sumDown = 0;
            for (FlowBean value : values) {
                sumDown += value.getDownFlow();
                sumUp += value.getUpFlow();
            }
            context.write(key, new FlowBean(sumUp, sumDown));
        }
    }
}
