package com.hiscat.flume.inerceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

/**
 * @author hiscat
 */
@Slf4j
public class TopicInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new TopicInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }

    @Override
    public Event intercept(Event event) {
        if (event == null) {
            return null;
        }
        String body = new String(event.getBody());
        if (body.contains("hello")) {
            LOGGER.info("body:{}", body);
            event.getHeaders().put("topic", "hello");
        } else if (body.contains("hiscat")) {
            event.getHeaders().put("topic", "hiscat");
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
}
