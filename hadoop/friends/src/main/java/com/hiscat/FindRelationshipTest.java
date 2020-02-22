package com.hiscat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author hiscat
 */
public class FindRelationshipTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\friends\\src\\main\\resources\\output\\part-r-00000"));
        job.setMapperClass(FindRelationshipMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\friends\\src\\main\\resources\\result");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setReducerClass(FindRelationshipReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setJarByClass(FindRelationshipTest.class);
        job.waitForCompletion(true);
    }

    private static class FindRelationshipMapper extends Mapper<LongWritable, Text, Text, Text> {

        private Text value = new Text();
        private Text key = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            final String[] friends = split[1].split(",");
            Arrays.sort(friends);

            this.value.set(split[0]);
            for (int i = 0; i < friends.length; i++) {
                for (int j = i + 1; j < friends.length; j++) {
                    this.key.set(friends[i] + "-" + friends[j]);
                    context.write(this.key, this.value);
                }
            }
        }
    }

    private static class FindRelationshipReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuilder result = new StringBuilder();
            for (Text value : values) {
                result.append(value.toString()).append(",");
            }
            context.write(key, new Text(result.substring(0, result.length() - 1)));
        }
    }
}
