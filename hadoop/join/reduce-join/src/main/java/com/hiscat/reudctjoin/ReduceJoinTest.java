package com.hiscat.reudctjoin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author hiscat
 */
public class ReduceJoinTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\join\\reduce-join\\src\\main\\resources\\input"));
        job.setMapperClass(OrderBeanMapper.class);
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\join\\reduce-join\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setReducerClass(OrderBeanReducer.class);
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);
        job.setGroupingComparatorClass(OrderGroupingComparator.class);

        job.setJarByClass(ReduceJoinTest.class);
        job.waitForCompletion(true);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class OrderBean implements WritableComparable<OrderBean> {
        private String orderId;
        private String productId;
        private Integer amount;
        private String productName;

        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(orderId);
            out.writeUTF(productId);
            out.writeInt(amount);
            out.writeUTF(productName);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            orderId = in.readUTF();
            productId = in.readUTF();
            amount = in.readInt();
            productName = in.readUTF();
        }

        @Override
        public int compareTo(ReduceJoinTest.OrderBean o) {
            final int compare = this.productId.compareTo(o.productId);
            return compare == 0 ? o.productName.compareTo(this.productName) : compare;
        }
    }

    @Slf4j
    private static class OrderBeanMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {

        private OrderBean k = new OrderBean();
        private String name;

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            name = ((FileSplit) context.getInputSplit()).getPath().getName();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            k = new OrderBean();
            if ("order.txt".equals(this.name)) {
                k
                        .setOrderId(split[0])
                        .setProductId(split[1])
                        .setProductName("")
                        .setAmount(Integer.valueOf(split[2]));
            } else if ("product.txt".equals(this.name)) {

                k
                        .setOrderId("")
                        .setProductId(split[0])
                        .setProductName(split[1])
                        .setAmount(0);
            }

            context.write(k, NullWritable.get());
        }
    }

    @Slf4j
    private static class OrderBeanReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            System.out.println(key);
            final Iterator<NullWritable> iterator = values.iterator();
            iterator.next();

            iterator.next();
            System.out.println(key);
            System.out.println();
        }

    }

    private static class OrderGroupingComparator extends WritableComparator {
        public OrderGroupingComparator() {
            super(OrderBean.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            OrderBean bean1 = (OrderBean) a;
            OrderBean bean2 = (OrderBean) b;
            return bean1.productId.compareTo(bean2.productId);
        }
    }
}
