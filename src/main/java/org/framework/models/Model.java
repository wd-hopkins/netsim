package org.framework.models;

public abstract class Model {
    public String id;

    public Model(String id) {
        this.id = id;
    }

    public abstract void onSelect();

    @Override
    public String toString() {
        return this.id;
    }
}
