package org.netsim.models;

import org.netsim.cli.Option;

public class SynchronousModel extends Model {

    public static String modelId = "Synchronous";
    @Option(name = "max_transmission_delay",
            description = "Time (ms) that messages are guaranteed to arrive by.")
    private int delay;

    public SynchronousModel() {
        this.delay = 0;
    }

    @Override
    public void run() {
        this.nodes.forEach(node -> node.setDelay(this.delay));
        nodes.get(0).init();
    }

    @Override
    public String toString() {
        return modelId;
    }
}