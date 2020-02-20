package com.histcat.kv.inputformat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.KeyValueLineRecordReader;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author hiscat
 */
public class KvInputFormatTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        final Configuration conf = new Configuration();
        conf.set(KeyValueLineRecordReader.KEY_VALUE_SEPERATOR, " ");
        final Job job = Job.getInstance(conf);

        //in
        KeyValueTextInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\kv-input-format\\src\\main\\resources"));
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\kv-input-format\\src\\main\\resources\\output"));
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //run
        job.setJarByClass(KvInputFormatTest.class);
        job.waitForCompletion(true);
    }

    private static class WordCountMapper extends Mapper<Text, Text, Text, IntWritable> {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            context.write(key, new IntWritable());
        }
    }

    private static class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable value : values) {
                sum++;
            }
            context.write(key, new IntWritable(sum));
        }
    }
}
