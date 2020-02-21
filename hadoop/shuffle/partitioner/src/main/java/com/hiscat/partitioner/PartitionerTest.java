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

/**
 * @author hiscat
 */
public class PartitionerTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\shuffle\\partitioner\\src\\main\\resources\\phone.txt"));
        job.setMapperClass(ProvinceMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setPartitionerClass(ProvincePartitioner.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\shuffle\\partitioner\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setReducerClass(ProvinceReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        job.setNumReduceTasks(5);

        job.setJarByClass(PartitionerTest.class);
        job.waitForCompletion(true);

    }


    @Slf4j
    private static class ProvinceMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] s = value.toString().split("\t");
            LOGGER.info("length:{}", s.length);
            context.write(new Text(s[1]), new Text(s[s.length - 3] + " " + s[s.length - 2]));
        }
    }


    private static class ProvincePartitioner extends Partitioner<Text, Text> {

        @Override
        public int getPartition(Text key, Text text2, int numPartitions) {
            final String prefix = key.toString().substring(0, 3);
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

    private static class ProvinceReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(key, value);
            }
        }

    }
}
