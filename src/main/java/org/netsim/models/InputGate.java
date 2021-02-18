package org.netsim.models;

import org.netsim.util.ObservableQueue;

import java.util.LinkedList;

public class InputGate {

    public OutputGate connection;
    public ObservableQueue<Object> buffer = new ObservableQueue<>(new LinkedList<>());

    public InputGate() {

    }

    public Object poll() {
        return this.buffer.poll();
    }

    public void addListener(ObservableQueue.Listener<Object> listener) {
        this.buffer.registerListener(listener);
    }

}
