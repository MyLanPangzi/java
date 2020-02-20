package com.hiscat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author hiscat
 */
public class MapJoinTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\join\\map-join\\src\\main\\resources\\input\\order.txt"));
        job.setMapperClass(OrderMapper.class);
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.addCacheFile(Paths.get("E:\\github\\java\\hadoop\\join\\map-join\\src\\main\\resources\\input\\product.txt").toUri());

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\join\\map-join\\src\\main\\resources\\output");
        FileSystem.get(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setNumReduceTasks(0);

        job.setJarByClass(MapJoinTest.class);
        job.waitForCompletion(true);

    }

    @Data
    @NoArgsConstructor
    @Accessors(chain = true)
    private static class OrderBean implements Writable {
        private String orderId;
        private String productName;
        private Double amount;

        @Override
        public String toString() {
            return String.format("%s\t%s\t%s\t", orderId, productName, amount);
        }


        @Override
        public void write(DataOutput out) throws IOException {
            out.writeUTF(orderId);
            out.writeUTF(productName);
            out.writeDouble(amount);
        }

        @Override
        public void readFields(DataInput in) throws IOException {
            this.orderId = in.readUTF();
            this.productName = in.readUTF();
            this.amount = in.readDouble();
        }
    }

    private static class OrderMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {

        private HashMap<String, String> caches;
        private OrderBean bean = new OrderBean();

        @Override
        protected void setup(Context context) throws IOException {
            final URI cacheFile = context.getCacheFiles()[0];
            try (FSDataInputStream in = FileSystem.get(context.getConfiguration()).open(new Path(cacheFile))) {
                caches = IOUtils.readLines(in)
                        .stream()
                        .map(s -> s.split("\t"))
                        .collect(HashMap<String, String>::new, (map, s) -> map.put(s[0], s[1]), HashMap::putAll);
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            bean.setOrderId(split[0])
                    .setProductName(caches.get(split[1]))
                    .setAmount(Double.valueOf(split[2]));
            context.write(bean, NullWritable.get());
        }
    }

}
