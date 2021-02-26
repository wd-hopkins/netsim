package org.netsim.models;

public class SynchronousModel extends Model {

    public static String modelId = "Synchronous";
    public float threshold;

    public SynchronousModel() {

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
