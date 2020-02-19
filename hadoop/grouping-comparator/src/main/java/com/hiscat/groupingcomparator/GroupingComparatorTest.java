package com.hiscat.groupingcomparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author hiscat
 */
public class GroupingComparatorTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\grouping-comparator\\src\\main\\resources\\order.txt"));
        job.setMapperClass(OrderBeanMapper.class);
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        // out
        job.setReducerClass(OrderBeanReducer.class);
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);
        job.setGroupingComparatorClass(OrderBeanGroupingComparator.class);
        final Path outputDir = new Path("E:\\github\\java\\hadoop\\grouping-comparator\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);

        // run
        job.setJarByClass(GroupingComparatorTest.class);
        job.waitForCompletion(true);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OrderBean implements Writable, WritableComparable<OrderBean> {
        private String id;
        private Double price;

        @Override
        public String toString() {
            return String.format("%s\t%s\t", id, price);
        }

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(id);
            out.writeDouble(price);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            this.id = in.readUTF();
            this.price = in.readDouble();
        }

        @Override
        public int compareTo(GroupingComparatorTest.OrderBean o) {
            final int i = this.id.compareTo(o.id);
            if (i == 0) {
                return -this.price.compareTo(o.price);
            }
            return i;
        }
    }

    @Slf4j
    private static class OrderBeanMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            final OrderBean bean = new OrderBean(split[0], Double.valueOf(split[2]));
            LOGGER.info("length:{},split:{}", split.length, Arrays.toString(split));
            context.write(bean, NullWritable.get());
        }
    }

    @Slf4j
    private static class OrderBeanReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            LOGGER.info("key:{}", key);
            context.write(key, NullWritable.get());
        }
    }

    @Slf4j
    private static class OrderBeanGroupingComparator extends WritableComparator {
        protected OrderBeanGroupingComparator() {
            super(OrderBean.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            LOGGER.info("orderBean1:{},orderBean2:{}", a, b);
            OrderBean orderBean1 = (OrderBean) a;
            OrderBean orderBean2 = (OrderBean) b;
            return orderBean1.id.compareTo(orderBean2.id);
        }
    }
}
