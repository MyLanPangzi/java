package com.hiscat.groupingcomparator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

/**
 * @author hiscat
 */
public class GroupingComparatorTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\shuffle\\grouping-comparator\\src\\main\\resources\\order.txt"));
        job.setMapperClass(OrderBeanMapper.class);
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\shuffle\\grouping-comparator\\src\\main\\resources\\output");
        FileOutputFormat.setOutputPath(job, outputDir);
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        job.setReducerClass(OrderBeanReducer.class);
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);
        job.setGroupingComparatorClass(OrderBeanGroupingComparator.class);

        job.setJarByClass(GroupingComparatorTest.class);
        job.waitForCompletion(true);

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class OrderBean implements WritableComparable<OrderBean> {
        private String id;
        private Double price;

        @Override
        public int compareTo(GroupingComparatorTest.OrderBean o) {
            final int i = this.id.compareTo(o.id);
            return i == 0 ? o.price.compareTo(this.price) : i;
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
        public String toString() {
            return String.format("%s\t%s\t", id, price);
        }
    }

    private static class OrderBeanMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            context.write(new OrderBean(split[0], Double.valueOf(split[2])), NullWritable.get());
        }
    }

    private static class OrderBeanReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {
        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }


    private static class OrderBeanGroupingComparator extends WritableComparator {
        public OrderBeanGroupingComparator() {
            super(OrderBean.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            OrderBean bean1 = (OrderBean) a;
            OrderBean bean2 = (OrderBean) b;
            return bean1.id.compareTo(bean2.id);
        }
    }
}
