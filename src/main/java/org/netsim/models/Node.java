package org.netsim.models;

import lombok.Getter;
import lombok.Setter;
import org.netsim.cli.CommandShell;
import org.netsim.ui.GUIApplication;
import org.netsim.util.ClassUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Node {

    private final Class<?> context;
    private final ScheduledExecutorService threadPool;
    private @Setter long delay;
    protected @Getter List<InputGate> in;
    protected @Getter List<OutputGate> out;
    public String name;

    public Node(String name) {
        this.in = Collections.singletonList(new InputGate("in"));
        this.out = Collections.singletonList(new OutputGate("out"));
        this.context = ClassUtil.getContextClass();
        this.threadPool = context == CommandShell.class ? CommandShell.getRunner().getThreadPool() : GUIApplication.getRunner().getThreadPool();
        this.delay = 0;
        this.name = name;
        addListeners();
    }

    private void addListeners() {
        this.in.forEach(x -> x.addListener(e -> this.onReceive(x.poll())));
    }

    public final void createInGates(List<String> gates) {
        List<InputGate> newGates = new ArrayList<>();
        for (String name : gates) {
            newGates.add(new InputGate(name));
        }
        this.in = newGates;
        addListeners();
    }

    public final void createOutGates(List<String> gates) {
        List<OutputGate> newGates = new ArrayList<>();
        for (String name : gates) {
            newGates.add(new OutputGate(name));
        }
        this.out = newGates;
    }

    /**
     * Connect output gate of this node to the input gate of another node.
     */
    public final void connect(String outName, InputGate inputGate) {
        OutputGate gate = getOutputGateByName(outName);
        if (gate != null) {
            gate.connection = inputGate;
            inputGate.connection = gate;
        } else {
            throw new IllegalArgumentException("Gate not defined: " + outName);
        }
    }

    public final OutputGate getOutputGateByName(String name) {
        for (OutputGate gate : out) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        return null;
    }

    public final InputGate getInputGateByName(String name) {
        for (InputGate gate : in) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        return null;
    }

    public void init() {
        send("Please override the init method of the Node superclass.");
    }

    public void onReceive(Object message) {
        System.out.printf("[%s] Received: %s\n", this.name, message);
    }

    protected final void send(Object message) {
        send(message, this.delay);
    }

    protected final void send(Object message, long delay) {
        this.out.forEach(x -> x.send(message, this.threadPool, delay));
    }

    protected final void send(Object message, String gateName) {
        send(message, gateName, this.delay);
    }

    protected final void send(Object message, String gateName, long delay) {
        OutputGate gate = getOutputGateByName(gateName);
        if (gate != null) {
            gate.send(message, this.threadPool, delay);
        } else {
            throw new IllegalArgumentException("Invalid gate name: " + gateName);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
