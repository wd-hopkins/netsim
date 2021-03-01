package org.netsim.models;

import lombok.Getter;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OutputGate {

    private final @Getter String name;
    public InputGate connection = null;

    public OutputGate(String name) {
        this.name = name;
    }

    public void send(Object payload, ScheduledExecutorService pool, long delay) {
        long gauss = (long) new Random().nextGaussian() * delay;
        pool.schedule(() -> this.connection.buffer.offer(payload), gauss, TimeUnit.MILLISECONDS);
    }
}
