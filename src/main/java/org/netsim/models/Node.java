package org.netsim.models;

import lombok.Getter;

import java.util.LinkedList;
import java.util.Queue;

public class Node {

    private final @Getter Gate in = Gate.IN;
    private final @Getter Gate out = Gate.OUT;
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
        this.out.connection.buffer.add("Hello!");
    }

    public String receive() {
        return String.format("[%s] Received: %s", this.name, this.in.buffer.poll());
    }

    enum Gate {
        IN,
        OUT;

        private Gate connection;

        private Queue<String> buffer = new LinkedList<>();
    }

}
