package com.hiscat.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author hiscat
 */
public class LogTypeInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        if (new String(event.getBody(), StandardCharsets.UTF_8).contains(LogUtils.START_LOG)) {
            event.getHeaders().put("topic", "topic_start");
        } else {
            event.getHeaders().put("topic", "topic_event");
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        list.forEach(this::intercept);
        return list;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new LogTypeInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
