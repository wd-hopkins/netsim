package org.netsim.models;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.Queue;

public class Node {

    protected final @Getter Gate in = Gate.IN;
    protected final @Getter Gate out = Gate.OUT;
    public String name;

    public Node(String name) {
        this.name = name;
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
            System.out.println("The given gate must be an input gate");
        }
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

    public String receive() {
        return String.format("[%s] Received: %s", this.name, this.in.buffer.poll());
    }

    protected enum Gate {
        IN,
        OUT;

        public Gate connection;

        public Queue<String> buffer = new LinkedList<>();
    }

}
