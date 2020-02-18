package com.hiscat.nline.inputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author hiscat
 */
public class NLineInputFormatTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\nline-input-format\\src\\main\\resources\\3"));
        NLineInputFormat.setNumLinesPerSplit(job, 1);
        job.setInputFormatClass(NLineInputFormat.class);
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\nline-input-format\\src\\main\\resources\\output"));
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //run
        job.setJarByClass(NLineInputFormatTest.class);
        job.waitForCompletion(true);
    }

    private static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                context.write(new Text(tokenizer.nextToken()), new IntWritable(1));
            }
        }
    }

}
