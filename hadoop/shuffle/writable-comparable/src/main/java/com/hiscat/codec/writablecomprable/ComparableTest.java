package com.hiscat.codec.writablecomprable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
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
public class ComparableTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\writable-comparable\\src\\main\\resources\\phone.txt"));
        job.setMapperClass(FlowBeanMapper.class);
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\writable-comparable\\src\\main\\resources\\output");
        FileOutputFormat.setOutputPath(job, outputDir);
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        job.setReducerClass(FlowBeanReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        job.setJarByClass(ComparableTest.class);
        job.waitForCompletion(true);

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class FlowBean implements WritableComparable<FlowBean> {

        private Long upFlow;
        private Long downFlow;
        private Long sumFlow;

        public FlowBean(Long upFlow, Long downFlow) {
            this.upFlow = upFlow;
            this.downFlow = downFlow;
            this.sumFlow = this.upFlow + this.downFlow;
        }

        @Override
        public int compareTo(ComparableTest.FlowBean o) {
            return o.sumFlow.compareTo(this.sumFlow);
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

    private static class FlowBeanMapper extends Mapper<LongWritable, Text, FlowBean, Text> {

        private Text value = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            this.value.set(split[1]);
            context.write(new FlowBean(Long.valueOf(split[split.length - 3]), Long.valueOf(split[split.length - 2])), this.value);
        }
    }

    private static class FlowBeanReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(value, key);
            }
        }
    }
}
