package org.netsim.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OutputGate {

    private final @Getter String name;
    private final ScheduledExecutorService threadpool;
    public InputGate connection = null;
    private @Setter long waitingDelay;
    private @Setter long maxTransmissionDelay;
    private volatile @Getter ScheduledFuture<?> latestEvent;

    public OutputGate(String name, ScheduledExecutorService pool) {
        this.name = name;
        this.threadpool = pool;
        this.waitingDelay = 0;
        this.maxTransmissionDelay = 0;
    }

    private double inverseGaussian() {
        double mu = 0.05;
        double lambda = 1;

        Random rand = new Random();
        double var = rand.nextGaussian();
        double y = var * var;
        double x = mu + (mu * mu * y) / (2 * lambda) - (mu / (2 * lambda)) * Math.sqrt(4 * mu * lambda * y + mu * mu * y * y);
        double test = rand.nextDouble();
        if (test <= (mu) / (mu + x))
            return x;
        else
            return (mu * mu) / x;
    }

    private void _send(Object payload, long maxDelay, long delay) {
        double invGauss = inverseGaussian() * 1000;
        if (invGauss > maxDelay) {
            invGauss = maxDelay;
        }
        latestEvent = this.threadpool.schedule(() -> this.connection.buffer.offer(payload), (long) invGauss + delay, TimeUnit.MILLISECONDS);
    }

    public void send(Object payload) {
        _send(payload, this.maxTransmissionDelay, this.waitingDelay);
    }

    public void send(Object payload, long delay) {
        _send(payload, this.maxTransmissionDelay, delay);
    }
}
