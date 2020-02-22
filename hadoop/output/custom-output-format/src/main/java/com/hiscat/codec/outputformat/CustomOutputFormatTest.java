package com.hiscat.codec.outputformat;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author hiscat
 */
public class CustomOutputFormatTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path("E:\\github\\java\\hadoop\\output\\custom-output-format\\src\\main\\resources\\url.txt"));
        job.setMapperClass(UrlMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path("E:\\github\\java\\hadoop\\output\\custom-output-format\\src\\main\\resources\\output");
        FileOutputFormat.setOutputPath(job, outputDir);
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        job.setReducerClass(UrlReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        job.setOutputFormatClass(UrlOutputFormat.class);

        job.setJarByClass(CustomOutputFormatTest.class);
        job.waitForCompletion(true);
    }

    private static class UrlMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            context.write(value, NullWritable.get());
        }
    }

    private static class UrlReducer extends Reducer<Text, NullWritable, Text, NullWritable> {
        @Override
        protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            context.write(key, NullWritable.get());
        }
    }

    private static class UrlOutputFormat extends FileOutputFormat<Text, NullWritable> {
        @Override
        public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {

            return new CustomRecordWriter(job);
        }

        @Slf4j
        private static class CustomRecordWriter extends RecordWriter<Text, NullWritable> {


            private final FSDataOutputStream atguiguIn;
            private final FSDataOutputStream otherIn;

            public CustomRecordWriter(TaskAttemptContext job) throws IOException {
                final FileSystem fs = FileSystem.get(job.getConfiguration());
                atguiguIn = fs.create(new Path("E:\\github\\java\\hadoop\\output\\custom-output-format\\src\\main\\resources\\output\\atguigu.log"), true);
                otherIn = fs.create(new Path("E:\\github\\java\\hadoop\\output\\custom-output-format\\src\\main\\resources\\\\output\\other.log"), true);
            }

            @Override
            public void write(Text key, NullWritable value) throws IOException {
                final String val = key.toString();
                LOGGER.info("val:{}", val);
                if (val.startsWith("http://www.atguigu.com")) {
                    atguiguIn.write(key.getBytes());
                    atguiguIn.write("\n".getBytes());
                } else {
                    otherIn.write(key.getBytes());
                    otherIn.write("\n".getBytes());
                }

            }

            @Override
            public void close(TaskAttemptContext context) {
                IOUtils.closeStream(atguiguIn);
                IOUtils.closeStream(otherIn);
            }
        }
    }
}
