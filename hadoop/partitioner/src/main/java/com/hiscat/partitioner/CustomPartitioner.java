package com.hiscat.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author hiscat
 */
public class CustomPartitioner {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\partitioner\\src\\main\\resources\\phone.txt"));
        job.setMapperClass(ProvinceGroupingMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\partitioner\\src\\main\\resources\\output"));
        job.setPartitionerClass(ProvincePartitioner.class);
        job.setReducerClass(ProvinceGroupingReducer.class);
        job.setNumReduceTasks(5);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //run
        job.setJarByClass(CustomPartitioner.class);
        job.waitForCompletion(true);
    }

    @Slf4j
    private static class ProvincePartitioner extends Partitioner<Text, Text> {
        @Override
        public int getPartition(Text key, Text value, int numPartitions) {
            final String prefix = key.toString().substring(0, 3);
            LOGGER.info("key:{},prefix:{}", key, prefix);
            switch (prefix) {
                case "136":
                    return 1;
                case "137":
                    return 2;
                case "138":
                    return 3;
                case "139":
                    return 4;
                default:
                    return 0;
            }
        }
    }

    @Slf4j
    private static class ProvinceGroupingMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t", 3);
            LOGGER.info("split:{},length:{}", Arrays.toString(split), split.length);
            context.write(new Text(split[1]), new Text(split[2]));
        }
    }

    private static class ProvinceGroupingReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(key, value);
            }
        }
    }
}
