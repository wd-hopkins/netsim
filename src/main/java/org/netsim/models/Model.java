package org.netsim.models;

public abstract class Model {
    public String id;

    public Model(String id) {
        this.id = id;
    }

    public abstract void onSelect();

    public abstract void run();

    @Override
    public String toString() {
        return this.id;
    }
}
