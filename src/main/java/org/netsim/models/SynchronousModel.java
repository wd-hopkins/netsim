package org.netsim.models;

import org.netsim.cli.Option;

public class SynchronousModel extends Model {

    public static String modelId = "Synchronous";
    @Option(description = "Time (ms) that messages are guaranteed to arrive by.")
    private int threshold;

    public SynchronousModel() {
        this.threshold = 0;
    }

    @Override
    public void init(Class<?> nodeImpl) {

    }

    @Override
    public void run() {
        nodes.get(0).init();
    }

    @Override
    public String toString() {
        return modelId;
    }
}
