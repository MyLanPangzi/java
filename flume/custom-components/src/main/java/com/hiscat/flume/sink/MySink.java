package com.hiscat.flume.sink;

import lombok.extern.slf4j.Slf4j;
import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;

/**
 * @author hiscat
 */
@Slf4j
public class MySink extends AbstractSink implements Configurable {
    private String myProp;

    @Override
    public void configure(Context context) {
        this.myProp = context.getString("hello", "world");
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public Status process() throws EventDeliveryException {
        Status status = null;

        // Start transaction
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        txn.begin();
        try {
            // This try clause includes whatever Channel operations you want to do

            Event event = ch.take();
            LOGGER.info(" prop:{},headers:{},body:{}", myProp, event.getHeaders(), new String(event.getBody()));
            // Send the Event to the external repository.
            // storeSomeData(e);

            txn.commit();
            status = Status.READY;
        } catch (Throwable t) {
            txn.rollback();

            // Log exception, handle individual exceptions as needed

            status = Status.BACKOFF;

            // re-throw all Errors
            if (t instanceof Error) {
                throw (Error) t;
            }
        } finally {
            txn.close();
        }
        return status;
    }
}