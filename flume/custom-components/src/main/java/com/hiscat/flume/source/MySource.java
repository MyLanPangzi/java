package com.hiscat.flume.source;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author hiscat
 */
public class MySource extends AbstractSource implements Configurable, PollableSource {
    private String myProp;

    @Override
    public void configure(Context context) {
        this.myProp = context.getString("hi", "defaultValue");
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
        // Disconnect from external client and do any additional cleanup
        // (e.g. releasing resources or nulling-out field values) ..
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;

        try {
            TimeUnit.SECONDS.sleep(3);
            Event e = getEvent();
            getChannelProcessor().processEvent(e);
            status = Status.READY;
        } catch (Throwable t) {
            status = Status.BACKOFF;

            if (t instanceof Error) {
                throw (Error) t;
            }
        }
        return status;
    }

    private Event getEvent() {
        Event e = new SimpleEvent();
        e.setBody("hello".getBytes());
        Map<String, String> map = new HashMap<>();
        map.put("type", myProp);
        e.setHeaders(map);
        return e;
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }
}