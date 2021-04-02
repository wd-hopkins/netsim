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

    public void send(Object payload, ScheduledExecutorService pool) {
        send(payload, pool, this.delay);
    }

    public void send(Object payload, ScheduledExecutorService pool, long delay) {
        double invGauss = inverseGaussian() * 1000;
        if (invGauss > delay) {
            invGauss = delay;
        }
        pool.schedule(() -> this.connection.buffer.offer(payload), (long) invGauss, TimeUnit.MILLISECONDS);
    }
}
