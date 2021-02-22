package org.netsim.models;

import lombok.Getter;
import lombok.SneakyThrows;
import org.netsim.cli.CommandShell;
import org.netsim.ui.GUIApplication;
import org.netsim.util.ClassUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Node {

    private final Class<?> context;
    private final ExecutorService threadPool;
    public String name;
    protected @Getter List<InputGate> in;
    protected @Getter List<OutputGate> out;

    public Node(String name) {
        this.in = Collections.singletonList(new InputGate("in"));
        this.out = Collections.singletonList(new OutputGate("out"));
        this.context = ClassUtil.getContextClass();
        this.threadPool = context == CommandShell.class ? CommandShell.getRunner().getThreadPool() : GUIApplication.getRunner().getThreadPool();
        this.name = name;
        addListeners();
    }

    private void addListeners() {
        this.in.forEach(x -> x.addListener(e -> this.threadPool.submit(() -> this.onReceive(x.poll()))));
    }

    public void createInGates(List<String> gates) {
        List<InputGate> newGates = new ArrayList<>();
        for (String name : gates) {
            newGates.add(new InputGate(name));
        }
        this.in = newGates;
        addListeners();
    }

    public void createOutGates(List<String> gates) {
        List<OutputGate> newGates = new ArrayList<>();
        for (String name : gates) {
            newGates.add(new OutputGate(name));
        }
        this.out = newGates;
    }

    /**
     * Connect output gate of this node to the input gate of another node.
     */
    public void connect(String outName, InputGate inputGate) {
        OutputGate gate = getOutputGateByName(outName);
        if (gate != null) {
            gate.connection = inputGate;
            inputGate.connection = gate;
        } else {
            throw new IllegalArgumentException("Gate not defined: " + outName);
        }
    }

    public OutputGate getOutputGateByName(String name) {
        for (OutputGate gate : out) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        return null;
    }

    public InputGate getInputGateByName(String name) {
        for (InputGate gate : in) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        return null;
    }

    public void init() {
        send("You must have a file in the current directory that overrides the receive method of the Node class.");
    }

    public void onReceive(Object message) {
        System.out.printf("[%s] Received: %s\n", this.name, message);
    }

    public void send(Object message) {
        this.out.forEach(x -> x.send(message));
    }

    @SneakyThrows
    public void send(Object message, long delay) {
        Thread.sleep(delay);
        send(message);
    }

    public void send(Object message, String gateName) {
        OutputGate gate = getOutputGateByName(gateName);
        if (gate != null) {
            gate.send(message);
        } else {
            throw new IllegalArgumentException("Invalid gate name: " + gateName);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
