package com.hiscat.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author hiscat
 */
public class WordCount {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\worcount\\src\\main\\java\\com\\hiscat\\wordcount"));
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\worcount\\src\\main\\java\\com\\hiscat\\wordcount\\output"));
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //run
        job.setJarByClass(WordCount.class);
        job.waitForCompletion(true);
    }

    private static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

        Text outputKey = new Text();
        IntWritable outputVal = new IntWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                outputKey.set(tokenizer.nextToken());
                outputVal.set(1);
                context.write(outputKey, outputVal);
            }
        }
    }

    private static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        IntWritable outputVal = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int result = 0;
            for (IntWritable value : values) {
                result += value.get();
            }
            this.outputVal.set(result);
            context.write(key, outputVal);
        }
    }
}
