package org.netsim.models;

public class OutputGate {

    public InputGate connection;

    public OutputGate() {

    }

    public void send(Object payload) {
        this.connection.buffer.offer(payload);
    }
}
