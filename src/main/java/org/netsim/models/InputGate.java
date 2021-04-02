package org.netsim.models;

import lombok.Getter;
import org.netsim.util.ObservableQueue;

import java.util.LinkedList;

public class InputGate {

    private final @Getter String name;
    public ObservableQueue<Object> buffer = new ObservableQueue<>(new LinkedList<>());
    public OutputGate connection = null;

    public InputGate(String name) {
        this.name = name;
    }

    public Object poll() {
        return this.buffer.poll();
    }

    public void setListener(ObservableQueue.Listener<Object> listener) {
        this.buffer.registerListener(listener);
    }

}
