package org.netsim.models;

import org.netsim.cli.Option;

public class SynchronousModel extends Model {

    public static String modelId = "Synchronous";
    @Option(name = "max_delay",
            description = "Time (ms) that messages are guaranteed to arrive by.")
    private int delay;

    public SynchronousModel() {
        this.delay = 0;
    }

    @Override
    public boolean setup() {
        this.nodes.forEach(node -> node.out.forEach(gate -> {
            if (!gate.isCustomMaxDelay()) {
                gate.setMaxTransmissionDelay(this.delay);
            }
        }));
        return true;
    }

    @Override
    public String toString() {
        return modelId;
    }
}
