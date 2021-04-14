package org.netsim.models;

import lombok.Getter;
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
    protected volatile @Getter List<InputGate> in;
    protected volatile @Getter List<OutputGate> out;
    public String name;

    public Node(String name) {
        this.context = ClassUtil.getContextClass();
        this.threadPool = context == CommandShell.class ? CommandShell.getRunner().getThreadPool() : GUIApplication.getRunner().getThreadPool();
        this.in = Collections.singletonList(new InputGate("in"));
        this.out = Collections.singletonList(new OutputGate("out", this.threadPool));
        this.name = name;
        addListeners();
    }

    private void addListeners() {
        this.in.forEach(x -> x.setListener(e -> this.onReceive(x.poll())));
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
            newGates.add(new OutputGate(name, this.threadPool));
        }
        this.out = newGates;
    }

    /**
     * Connect output gate of this node to the input gate of another node.
     */
    public final void connect(String outName, InputGate inputGate) {
        OutputGate gate = getOutputGateByName(outName);
        gate.connection = inputGate;
        inputGate.connection = gate;
    }

    public final OutputGate getOutputGateByName(String name) {
        for (OutputGate gate : out) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        throw new IllegalArgumentException("Gate not defined: " + name);
    }

    public final InputGate getInputGateByName(String name) {
        for (InputGate gate : in) {
            if (gate.getName().equals(name)) {
                return gate;
            }
        }
        throw new IllegalArgumentException("Gate not defined: " + name);
    }

    public void halt() {
        this.in.forEach(x -> x.setListener(e -> {}));
    }

    public void init() {
        send("Please override the init method of the Node superclass.");
    }

    public void onReceive(Object message) {
        System.out.printf("[%s] Received: %s\n", this.name, message);
    }

    public void setDelay(long delay) {
        this.out.forEach(x -> x.setWaitingDelay(delay));
    }

    public void setDelay(String name, long delay) {
        OutputGate out = getOutputGateByName(name);
        out.setWaitingDelay(delay);
    }
    
    public void setMaxTransmissionDelay(long delay) {
        this.out.forEach(x -> x.setMaxTransmissionDelay(delay));
    }
    
    public void setMaxTransmissionDelay(String name, long delay) {
        OutputGate out = getOutputGateByName(name);
        out.setMaxTransmissionDelay(delay);
    }

    protected final void send(Object message) {
        this.out.forEach(x -> x.send(message));
    }

    protected final void send(Object message, long delay) {
        this.out.forEach(x -> x.send(message, delay));
    }

    protected final void send(Object message, String gateName) {
        OutputGate gate = getOutputGateByName(gateName);
        gate.send(message);
    }

    protected final void send(Object message, OutputGate gate) {
        gate.send(message);
    }

    protected final void send(Object message, String gateName, long delay) {
        OutputGate gate = getOutputGateByName(gateName);
        gate.send(message, delay);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
