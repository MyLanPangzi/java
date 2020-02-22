package com.hiscat;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author hiscat
 */
public class FriendReducer  extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuilder result = new StringBuilder();
        for (Text value : values) {
            result.append(value.toString()).append(",");
        }
        context.write(key, new Text(result.substring(0, result.length() - 1)));
    }
}