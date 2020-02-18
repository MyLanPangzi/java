package com.hiscat.custom.inputformat;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
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
//        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //run
        job.setJarByClass(CustomInputFormatTest.class);
        job.waitForCompletion(true);
    }

    @Slf4j
    private static class WordCountMapper extends Mapper<Text, BytesWritable, Text, IntWritable> {
        @Override
        protected void map(Text key, BytesWritable value, Context context) throws IOException, InterruptedException {
            final String val = new String(value.getBytes());
            LOGGER.info("val:{}", val);
            StringTokenizer tokenizer = new StringTokenizer(val);
            while (tokenizer.hasMoreTokens()) {
                context.write(new Text(tokenizer.nextToken()), new IntWritable(1));
            }
        }
    }

    @Slf4j
    private static class WholeFileInputFormat extends FileInputFormat<Text, BytesWritable> {
        @Override
        protected boolean isSplitable(JobContext context, Path filename) {
            return false;
        }

        @Override
        public RecordReader<Text, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
            WholeRecordReader reader = new WholeRecordReader();
            reader.initialize(split, context);
            return reader;
        }

        private static class WholeRecordReader extends RecordReader<Text, BytesWritable> {

            private Configuration configuration;
            private FileSplit split;

            private BytesWritable value = new BytesWritable();
            private Text key = new Text();
            private boolean readed = false;


            @Override
            public void initialize(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
                this.split = (FileSplit) split;

                configuration = context.getConfiguration();
            }

            @Override
            public boolean nextKeyValue() throws IOException, InterruptedException {
                if (readed) {
                    return false;
                }
                try (FSDataInputStream in = split.getPath().getFileSystem(configuration).open(split.getPath());) {
                    LOGGER.info("path:{},length:{}", split.getPath(), split.getLength());
                    key.set(split.getPath().toString());
                    byte[] buff = new byte[(int) split.getLength()];
                    IOUtils.readFully(in, buff, 0, buff.length);
                    value.set(buff, 0, buff.length);
                    readed = true;
                    return true;
                } catch (Exception e) {
                    LOGGER.info("error:{}", e.getMessage(), e);
                }
                return false;
            }

            @Override
            public Text getCurrentKey() throws IOException, InterruptedException {
                return key;
            }

            @Override
            public BytesWritable getCurrentValue() throws IOException, InterruptedException {
                return value;
            }

            @Override
            public float getProgress() throws IOException, InterruptedException {
                return 0;
            }

            @Override
            public void close() throws IOException {

            }
        }
    }
}
