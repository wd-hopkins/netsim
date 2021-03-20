package org.netsim.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OutputGate {

    private final @Getter String name;
    public InputGate connection = null;
    private @Setter long delay;

    public OutputGate(String name) {
        this.name = name;
        this.delay = 0;
    }

    public void send(Object payload, ScheduledExecutorService pool) {
        send(payload, pool, this.delay);
    }

    public void send(Object payload, ScheduledExecutorService pool, long delay) {
        long gauss = (long) new Random().nextGaussian() * delay;
        pool.schedule(() -> this.connection.buffer.offer(payload), gauss, TimeUnit.MILLISECONDS);
    }
}
