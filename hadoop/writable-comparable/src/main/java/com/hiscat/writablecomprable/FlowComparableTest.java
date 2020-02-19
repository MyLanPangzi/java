package com.hiscat.writablecomprable;

import lombok.Data;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
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
public class FlowComparableTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\writable-comparable\\src\\main\\resources\\phone.txt"));
        job.setMapperClass(FlowCountMapper.class);
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\writable-comparable\\src\\main\\resources\\output"));
        job.setReducerClass(FlowCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //run
        job.setJarByClass(FlowComparableTest.class);
        job.waitForCompletion(true);
    }

    @Data
    private static class FlowBean implements Writable, WritableComparable<FlowBean> {
        private Long upFlow;
        private Long downFlow;
        private Long sumFlow;

        public FlowBean() {
        }

        public FlowBean(Long upFlow, Long downFlow) {
            this.upFlow = upFlow;
            this.downFlow = downFlow;
            this.sumFlow = this.upFlow + this.downFlow;
        }

        @Override
        public String toString() {
            return String.format("%s\t%s\t%s", upFlow, downFlow, sumFlow);
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
        public int compareTo(FlowComparableTest.FlowBean o) {

            return -this.sumFlow.compareTo(o.sumFlow);
        }
    }

    private static class FlowCountMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            context.write(new FlowBean(Long.parseLong(split[split.length - 3]), Long.parseLong(split[split.length - 2])), new Text(split[1]));
        }
    }

    private static class FlowCountReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(value, key);
            }
        }
    }
}
