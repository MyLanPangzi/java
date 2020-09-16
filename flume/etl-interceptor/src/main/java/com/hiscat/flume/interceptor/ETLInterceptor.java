package com.hiscat.flume.interceptor;

import com.alibaba.fastjson.JSONValidator;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author hiscat
 */
public class ETLInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        return JSONValidator.from(new String(event.getBody())).validate() ? event : null;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        return events
                .stream()
                .map(this::intercept)
                .collect(toList());
    }


    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new ETLInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
