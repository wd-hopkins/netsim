package org.netsim.models;

public class EmptyModel extends Model {

    public EmptyModel() {

    }

    @Override
    public void start() {
        System.out.println("Choose a model first");
    }

}
