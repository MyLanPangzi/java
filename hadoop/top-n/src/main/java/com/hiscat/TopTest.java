package com.hiscat;

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
import java.util.Map;
import java.util.TreeMap;

/**
 * @author hiscat
 */
public class TopTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\top-n\\src\\main\\resources\\input"));
        job.setMapperClass(TopMapper.class);
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\top-n\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setReducerClass(TopReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        job.setJarByClass(TopTest.class);
        job.waitForCompletion(true);
    }

    private static class TopMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        private TreeMap<FlowBean, String> top = new TreeMap<>();
        Text val = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            top.put(new FlowBean(Long.valueOf(split[split.length - 2]), Long.valueOf(split[split.length - 1])), split[0]);
            if (top.size() > 10) {
                top.remove(top.firstKey());
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (Map.Entry<FlowBean, String> entry : top.entrySet()) {
                val.set(entry.getValue());
                context.write(entry.getKey(), val);
            }
        }
    }

    private static class TopReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
        private TreeMap<FlowBean, String> top = new TreeMap<>();
        private Text val = new Text();

        @Override
        protected void reduce(FlowBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                top.put(new FlowBean(key.getUpFlow(), key.getDownFlow()), value.toString());
                if (top.size() > 10) {
                    top.remove(top.firstKey());
                }
            }
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (Map.Entry<FlowBean, String> entry : top.entrySet()) {
                val.set(entry.getValue());
                context.write(val, entry.getKey());
            }
        }
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
        public int compareTo(FlowBean o) {
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

}
