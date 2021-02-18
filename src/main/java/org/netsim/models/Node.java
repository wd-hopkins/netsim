package org.netsim.models;

import lombok.Getter;
import lombok.SneakyThrows;
import org.netsim.cli.CommandShell;
import org.netsim.ui.GUIApplication;
import org.netsim.util.ClassUtil;

import java.util.concurrent.ExecutorService;

public class Node {

    protected final @Getter InputGate in;
    protected final @Getter OutputGate out;
    private final Class<?> context;
    private final ExecutorService threadPool;
    public String name;

    private Node() {
        this.in = new InputGate();
        this.out = new OutputGate();
        this.context = ClassUtil.getContextClass();
        this.threadPool = context == CommandShell.class ? CommandShell.getRunner().getThreadPool() : GUIApplication.getRunner().getThreadPool();
        this.in.buffer.registerListener(e -> this.threadPool.submit(() -> this.onReceive(this.receive()))); //TODO: fix null messages
    }

    public Node(String name) {
        this();
        this.name = name;
    }

    /**
     * Connect output gate of this node to the input gate of another node.
     */
    public void connect(InputGate in) {
        this.out.connection = in;
        in.connection = this.out;
    }

    public void init() {
        send("You must have a file in the current directory that overrides the receive method of the Node class.");
    }

    public void onReceive(String message) {
        System.out.printf("[%s] Received: %s\n", this.name, message);
    }

    public String receive() {
        return this.in.buffer.poll();
    }

    public void send(String message) {
        this.out.connection.buffer.offer(message);
    }

    @SneakyThrows
    public void send(String message, long delay) {
        Thread.sleep(delay);
        send(message);
    }

}
