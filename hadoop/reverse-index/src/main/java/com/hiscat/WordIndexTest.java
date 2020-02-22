package com.hiscat;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author hiscat
 */
public class WordIndexTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\reverse-index\\src\\main\\resources\\output\\part-r-00000"));
        job.setMapperClass(WordSumCounterMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WordCountBean.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\reverse-index\\src\\main\\resources\\result");
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
//        job.setNumReduceTasks(0);
        job.setReducerClass(WordSumCounterReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setJarByClass(WordIndexTest.class);
        job.waitForCompletion(true);
    }

    private static class WordSumCounterMapper extends Mapper<LongWritable, Text, Text, WordCountBean> {
        Text outKey = new Text();
        WordCountBean bean = new WordCountBean();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] split = value.toString().split("\t");
            bean.setWord(split[0]).setFilename(split[1]).setCount(Integer.valueOf(split[2]));
            outKey.set(split[0]);
            context.write(outKey, bean);
        }
    }

    private static class WordSumCounterReducer extends Reducer<Text, WordCountBean, Text, NullWritable> {

        private Text outKey = new Text();

        @Override
        protected void reduce(Text key, Iterable<WordCountBean> values, Context context) throws IOException, InterruptedException {
            StringBuilder result = new StringBuilder();
            result.append(key.toString()).append("\t");
            for (WordCountBean value : values) {
                result.append(value.getFilename()).append("-->").append(value.getCount()).append("\t");
            }
            outKey.set(result.toString());
            context.write(outKey, NullWritable.get());
        }
    }
}
