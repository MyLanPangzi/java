package com.hiscat.hive;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.json.JSONArray;

import static java.util.Arrays.asList;
import static org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory.getStandardStructObjectInspector;
import static org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory.javaStringObjectInspector;

/**
 * @author hiscat
 */
public class EventJsonUDTF extends GenericUDTF {
    @Override
    public StructObjectInspector initialize(StructObjectInspector argois) {
        return getStandardStructObjectInspector(asList("event_name", "event_json"),
                asList(javaStringObjectInspector, javaStringObjectInspector));
    }

    @Override
    public void process(Object[] args) throws HiveException {
        final String jsonStr = args[0].toString();
        if (StringUtils.isBlank(jsonStr)) {
            return;
        }
        JSONArray events = new JSONArray(jsonStr);
        for (int i = 0; i < events.length(); i++) {
            try {
                forward(asList(events.getJSONObject(i).getString("en"), events.getString(i)));
            } catch (Exception ignored) {
            }
        }

    }

    @Override
    public void close() throws HiveException {

    }
}
