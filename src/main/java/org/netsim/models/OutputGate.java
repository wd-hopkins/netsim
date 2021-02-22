package org.netsim.models;

import lombok.Getter;

public class OutputGate {

    private final @Getter String name;
    public InputGate connection;

    public OutputGate(String name) {
        this.name = name;
    }

    public void send(Object payload) {
        this.connection.buffer.offer(payload);
    }
}
