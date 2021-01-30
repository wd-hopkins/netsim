package org.netsim.models;

import lombok.Getter;

public class Node {

    @Getter
    private final Gate in = Gate.IN;
    @Getter
    private final Gate out = Gate.OUT;
    public String name;

    public Node(String name) {
        this.name = name;
    }

    public void connect(Gate in, Gate out) {
        if (in == Gate.IN && out == Gate.OUT) {
            in.connection = out;
            out.connection = in;
        } else {
            System.out.println("Input gate must be connected to an Output gate");
        }
    }

    public enum Gate {
        IN,
        OUT;

        @Getter
        private Gate connection;
    }

}
