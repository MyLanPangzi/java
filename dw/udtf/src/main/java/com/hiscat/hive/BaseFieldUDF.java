package com.hiscat.hive;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONObject;

/**
 * @author hiscat
 */
public class BaseFieldUDF extends UDF {
    public String evaluate(String line, String key) {
        final String[] split = line.split("\\|");
        if (split.length != 2 || StringUtils.isBlank(split[1])) {
            return "";
        }
        JSONObject jsonObject = new JSONObject(split[1]);
        switch (key) {
            case "st":
                return split[0];
            case "et":
                return jsonObject.has(key) ? jsonObject.getString(key) : "";
            default:
                final JSONObject cm = jsonObject.getJSONObject("cm");
                return cm.has(key) ? cm.getString(key) : "";
        }
    }

}
