package com.hiscat.hbase.api;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @author hiscat
 */
public class HbaseWriteFileMR implements Tool {
    private Configuration conf;

    @Override
    public int run(String[] args) throws Exception {
        Job job = Job.getInstance(this.conf);
        //in
        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setMapperClass(FileMapper.class);
        job.setMapOutputKeyClass(ImmutableBytesWritable.class);
        job.setMapOutputValueClass(Put.class);

        //out
        TableMapReduceUtil.initTableReducerJob("fruit_mr", FileReducer.class, job);

        //run
        job.setJarByClass(HbaseWriteFileMR.class);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    public static void main(String[] args) throws Exception {
        args = new String[]{"C:\\Users\\Administrator\\Downloads\\fruit.tsv"};
        final Configuration conf = HBaseConfiguration.create();
        conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        ToolRunner.run(conf, new HbaseWriteFileMR(), args);
    }

    public static class FileMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Put> {

        public static final byte[] NAME = "name".getBytes();
        public static final byte[] COLOR = "color".getBytes();
        public static final byte[] CF = "info".getBytes();
        private ImmutableBytesWritable outKey = new ImmutableBytesWritable();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            final String[] cells = value.toString().split("\t");
            outKey.set(cells[0].getBytes());
            context.write(outKey, new Put(outKey.get()).addColumn(CF, NAME, cells[1].getBytes()));
            context.write(outKey, new Put(outKey.get()).addColumn(CF, COLOR, cells[2].getBytes()));

        }
    }

    public static class FileReducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {
        @Override
        protected void reduce(ImmutableBytesWritable key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
            for (Put value : values) {
                context.write(NullWritable.get(), value);
            }
        }
    }
}
