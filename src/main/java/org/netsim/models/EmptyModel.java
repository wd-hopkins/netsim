package org.netsim.models;

public class EmptyModel extends Model {

    public EmptyModel() {

    }

    @Override
    public void init(Class<?> nodeImpl) {

    }

    @Override
    public void onSelect() {
        System.out.println("Choose a model first");
    }

    @Override
    public void run() {
        onSelect();
    }

}
