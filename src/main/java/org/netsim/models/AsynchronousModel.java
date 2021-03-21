package org.netsim.models;

public class AsynchronousModel extends Model {

    public static String modelId = "Asynchronous";

    public AsynchronousModel() {

    }

    @Override
    public void run() {
        this.nodes.get(0).init();
    }

    @Override
    public String toString() {
        return modelId;
    }
}
