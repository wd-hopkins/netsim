package org.netsim.models;

public class SynchronousModel extends Model {

    public SynchronousModel() {
        this.modelId = "synchronous";
    }

    @Override
    public void init(Class<?> nodeImpl) {

    }

    @Override
    public void onSelect() {

    }

    @Override
    public void run() {
        nodes.get(0).init();
    }
}
