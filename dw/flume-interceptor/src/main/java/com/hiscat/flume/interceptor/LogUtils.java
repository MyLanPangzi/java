package com.hiscat.flume.interceptor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.flume.Event;

import java.nio.charset.StandardCharsets;

/**
 * @author hiscat
 */
public class LogUtils {

    public static final String START_LOG = "start";
    public static final int TIMESTAMP_LENGTH = 13;
    public static final int LOG_LENGTH = 2;

    public static boolean validate(Event event) {
        if (event.getBody() == null) {
            return false;
        }

        final String log = new String(event.getBody(), StandardCharsets.UTF_8);
        if (log.contains(START_LOG)) {
            return log.startsWith("{") && log.endsWith("}");
        }

        final String[] split = log.split("\\|");
        if (split.length != LOG_LENGTH) {
            return false;
        }

        if (split[0].length() != TIMESTAMP_LENGTH || !NumberUtils.isDigits(split[0])) {
            return false;
        }

        return split[1].startsWith("{") && split[1].endsWith("}");
    }
}
