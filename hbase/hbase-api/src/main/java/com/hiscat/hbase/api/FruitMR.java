package com.hiscat.hbase.api;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;

/**
 * @author hiscat
 */
public class FruitMR {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance();
        Scan scan = new Scan();
        scan.setCacheBlocks(false);
        scan.setCaching(500);

        //设置Mapper，注意导入的是mapreduce包下的，不是mapred包下的，后者是老版本
        TableMapReduceUtil.initTableMapperJob(
                "fruit",
                scan,
                FruitMapper.class,
                ImmutableBytesWritable.class,
                Put.class,
                job
        );
        TableMapReduceUtil.initTableReducerJob("fruit_mr", FruitReducer.class, job);
        job.setNumReduceTasks(1);
        //run
        job.setJarByClass(FruitMR.class);
        job.waitForCompletion(true);
    }

    private static class FruitMapper extends TableMapper<ImmutableBytesWritable, Put> {
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            Put put = new Put(key.get());
            for (Cell cell : value.rawCells()) {
                if (!"info".equals(Bytes.toString(CellUtil.cloneFamily(cell)))) {
                    continue;
                }
                final String column = Bytes.toString(CellUtil.cloneQualifier(cell));
                if ("name".equals(column) || "color".equals(column)) {
                    put.add(cell);
                }
            }
            context.write(key, put);
        }
    }

    private static class FruitReducer extends TableReducer<ImmutableBytesWritable, Put, NullWritable> {
        @Override
        protected void reduce(ImmutableBytesWritable key, Iterable<Put> values, Context context) throws IOException, InterruptedException {
            for (Put value : values) {
                context.write(NullWritable.get(), value);
            }
        }
    }
}
