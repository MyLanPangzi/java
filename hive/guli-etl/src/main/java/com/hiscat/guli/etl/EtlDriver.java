package com.hiscat.guli.etl;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hiscat
 */
public class EtlDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();

        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setMapperClass(VideoMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        final Path outputDir = new Path(args[1]);
        outputDir.getFileSystem(job.getConfiguration()).delete(outputDir, true);
        FileOutputFormat.setOutputPath(job, outputDir);
        job.setNumReduceTasks(0);

        job.setJarByClass(EtlDriver.class);
        job.waitForCompletion(true);
    }

    public static String extract(String data) {
        final String[] split = data.split("\t");
        if (split.length < 9) {
            return null;
        }
        split[3] = split[3].replaceAll(" ", "");
        final String result = Arrays.stream(split, 0, 9).collect(Collectors.joining("\t"));
        if (split.length == 9) {
            return result;
        }
        return result + "\t" + Arrays.stream(split, 9, split.length).collect(Collectors.joining("&"));
    }

    private static class VideoMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        private Text outKey = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String extract = extract(Objects.toString(value, ""));
            if (StringUtils.isBlank(extract)) {
                return;
            }
            outKey.set(extract);
            context.write(outKey, NullWritable.get());
        }


    }
}
