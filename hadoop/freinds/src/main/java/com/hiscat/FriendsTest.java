package com.hiscat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author hiscat
 */
public class FriendsTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\reverse-index\\src\\main\\resources\\input"));
//        job.setMapperClass(WordCounterMapper.class);
//        job.setMapOutputKeyClass(WordCountBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\reverse-index\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
//        job.setReducerClass(WordCounterReducer.class);
//        job.setOutputKeyClass(WordCountBean.class);
        job.setOutputValueClass(NullWritable.class);

        job.setJarByClass(FriendsTest.class);
        job.waitForCompletion(true);
    }

}
