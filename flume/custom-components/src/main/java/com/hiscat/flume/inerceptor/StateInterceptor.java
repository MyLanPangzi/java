package com.hiscat.flume.inerceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;

/**
 * @author hiscat
 */
public class StateInterceptor implements Interceptor {
    @Override
    public void initialize() {

    }

    public static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new StateInterceptor();
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
        if (body.contains("atguigu")) {
            event.getHeaders().put("state", "logger");
        } else if (body.contains("hiscat")) {
            event.getHeaders().put("state", "file-rolling");
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
