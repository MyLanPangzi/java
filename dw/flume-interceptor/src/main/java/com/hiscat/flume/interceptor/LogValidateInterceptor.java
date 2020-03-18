package com.hiscat.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author hiscat
 */
public class LogValidateInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        return LogUtils.validate(event) ? event : null;
    }

    @Override
    public List<Event> intercept(List<Event> list) {
        return list.stream().filter(LogUtils::validate).collect(toList());
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new LogValidateInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
