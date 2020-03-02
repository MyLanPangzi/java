package com.hiscat.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * @author hiscat
 */
public class Lower extends UDF {

    public String evaluate(String in) {
        return in.toLowerCase();
    }

}
