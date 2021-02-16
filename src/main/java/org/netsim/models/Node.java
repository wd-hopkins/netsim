package org.netsim.models;

import lombok.Getter;
import lombok.SneakyThrows;
import org.netsim.util.ObservableQueue;

import java.util.LinkedList;

public class Node {

    protected final @Getter Gate in = Gate.IN;
    protected final @Getter Gate out = Gate.OUT;
    public String name;

    public Node(String name) {
        this.name = name;
        this.in.buffer.registerListener(e -> onReceive(receive()));
    }

    /**
     * Connect output gate of this node to the input gate of another node.
     * Requires input to be of type <code>Gate.IN</code>
     */
    public void connect(Gate in) {
        if (in == Gate.IN) {
            this.out.connection = in;
            in.connection = this.out;
        } else {
            throw new IllegalArgumentException("The given gate must be an input gate");
        }
    }

    public void init() {
        send();
    }

    public void onReceive(String message) {
        System.out.printf("[%s] Received: %s\n", this.name, message);
    }

    public String receive() {
        return this.in.buffer.poll();
    }

    public void send() {
        send("You must have a file in the current directory that overrides the receive method of the Node class.");
    }

    public void send(String message) {
        this.out.connection.buffer.add(message);
    }

    @SneakyThrows
    public void send(String message, long delay) {
        Thread.sleep(delay);
        send(message);
    }

    protected enum Gate {
        IN,
        OUT;

        public Gate connection;

        public ObservableQueue<String> buffer = new ObservableQueue<>(new LinkedList<>());
    }

}
