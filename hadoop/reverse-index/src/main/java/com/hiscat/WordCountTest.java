package com.hiscat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author hiscat
 */
public class WordCountTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\reverse-index\\src\\main\\resources\\input"));
        job.setMapperClass(WordCounterMapper.class);
        job.setMapOutputKeyClass(WordCountBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\reverse-index\\src\\main\\resources\\output");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setReducerClass(WordCounterReducer.class);
        job.setOutputKeyClass(WordCountBean.class);
        job.setOutputValueClass(NullWritable.class);

        job.setJarByClass(WordCountTest.class);
        job.waitForCompletion(true);
    }




    private static class WordCounterMapper extends Mapper<LongWritable, Text, WordCountBean, NullWritable> {
        private String filename;
        private WordCountBean k = new WordCountBean();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            final FileSplit split = (FileSplit) context.getInputSplit();
            this.filename = split.getPath().getName();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                this.k.setFilename(filename).setCount(1).setWord(tokenizer.nextToken());
                context.write(this.k, NullWritable.get());
            }
        }
    }


    private static class WordCounterReducer extends Reducer<WordCountBean, NullWritable, WordCountBean, NullWritable> {
        WordCountBean k = new WordCountBean();

        @Override
        protected void reduce(WordCountBean key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (NullWritable value : values) {
                sum += key.getCount();
                k.setFilename(key.getFilename()).setWord(key.getWord());
            }
            k.setCount(sum);
            context.write(k, NullWritable.get());
        }
    }

}
