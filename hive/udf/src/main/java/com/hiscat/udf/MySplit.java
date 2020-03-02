package com.hiscat.udf;

import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory.getStandardStructObjectInspector;
import static org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory.javaStringObjectInspector;

/**
 * @author hiscat
 */
public class MySplit extends GenericUDTF {

    private List<String> lines = new ArrayList<>();

    @Override
    public StructObjectInspector initialize(StructObjectInspector argois) {
        return getStandardStructObjectInspector(singletonList("split"), singletonList(javaStringObjectInspector));
    }

    @Override
    public void process(Object[] args) throws HiveException {
        final String str = args[0].toString();
        final String separator = args[1].toString();
        for (String s : str.split(separator)) {
            forward(singletonList(s));
        }
    }

    @Override
    public void close() {

    }
}
