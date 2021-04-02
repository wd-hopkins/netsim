package org.netsim.models;

import org.netsim.cli.Option;

public class PartialSynchronousModel extends Model {

    public static String modelId = "PartialSynchrony";
    @Option(name = "stab_time",
            description = "Time (ms) after which all of the connections between nodes will become synchronous.")
    private long globalStabTime;
    @Option(name = "max_delay",
            description = "Time (ms) that messages are guaranteed to arrive by.")
    private long delay;

    public PartialSynchronousModel() {
        this.globalStabTime = 0;
        this.delay = 0;
    }

    @Override
    public void run() {
        new Thread(() -> {
            try {
                Thread.sleep(globalStabTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.nodes.forEach(node -> node.setDelay(this.delay));
        }).start();
        this.nodes.get(0).init();
    }

    @Override
    public String toString() {
        return modelId;
    }
}