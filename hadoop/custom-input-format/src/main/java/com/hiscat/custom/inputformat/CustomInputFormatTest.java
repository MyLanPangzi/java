package com.hiscat.custom.inputformat;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.IntSumReducer;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author hiscat
 */
public class CustomInputFormatTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        //in
        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\custom-input-format\\src\\main\\resources"));
        job.setInputFormatClass(WholeFileInputFormat.class);
        job.setMapperClass(WordCountMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        //out
        FileOutputFormat.setOutputPath(job, new Path("E:\\github\\java\\hadoop\\custom-input-format\\src\\main\\resources\\output"));
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //run
        job.setJarByClass(CustomInputFormatTest.class);
        job.waitForCompletion(true);
    }

    @Slf4j
    private static class WordCountMapper extends Mapper<Text, Text, Text, IntWritable> {
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            LOGGER.info("val:{}", value.toString());
            StringTokenizer tokenizer = new StringTokenizer(value.toString());
            while (tokenizer.hasMoreTokens()) {
                context.write(new Text(tokenizer.nextToken()), new IntWritable(1));
            }
        }
    }

    @Slf4j
    private static class WholeFileInputFormat extends FileInputFormat<Text, Text> {
        @Override
        protected boolean isSplitable(JobContext context, Path file) {
            return false;
        }

        @Override
        public RecordReader<Text, Text> createRecordReader(InputSplit split, TaskAttemptContext context) {
            return new WholeFileRecordReader();
        }

        @Slf4j
        private static class WholeFileRecordReader extends RecordReader<Text, Text> {

            private FileSplit split;
            private FSDataInputStream in;
            private Text key;
            private Text value;
            private boolean hasNext;

            @Override
            public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
                LOGGER.info("initialize...");
                this.split = (FileSplit) split;
                in = this.split.getPath().getFileSystem(context.getConfiguration()).open(this.split.getPath());
                hasNext = true;
            }

            @Override
            public boolean nextKeyValue() throws IOException, InterruptedException {
                if (!hasNext) {
                    return false;
                }
                if (key == null) {
                    key = new Text(split.getPath().toString());
                }
                if (value == null) {
                    value = new Text();
                    value.readWithKnownLength(in, (int) split.getLength());
                }
                hasNext = false;
                return true;
            }

            @Override
            public Text getCurrentKey() throws IOException, InterruptedException {
                return key;
            }

            @Override
            public Text getCurrentValue() throws IOException, InterruptedException {
                return value;
            }

            @Override
            public float getProgress() throws IOException, InterruptedException {
                return 0;
            }

            @Override
            public void close() throws IOException {
                LOGGER.info("close...");
                IOUtils.closeStream(in);
            }
        }
    }
}
