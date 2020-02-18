package com.hiscat.inputformat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author hiscat
 */
public class CombineTextInputFormatTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        CombineTextInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\combine-text-input-format\\src\\main\\java\\com\\hiscat\\inputformat"));
        CombineTextInputFormat.setMaxInputSplitSize(job, 1024);
        job.setInputFormatClass(CombineTextInputFormat.class);
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setMapOutputKeyClass(Text.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\combine-text-input-format\\src\\main\\java\\com\\hiscat\\inputformat\\output"));
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        //run
        job.setJarByClass(CombineTextInputFormatTest.class);
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

    private static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable ignored : values) {
                sum++;
            }
            context.write(key, new IntWritable(sum));
        }
    }
}
