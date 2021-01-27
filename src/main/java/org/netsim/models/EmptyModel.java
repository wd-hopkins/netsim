package org.netsim.models;

public class EmptyModel extends Model {

    public EmptyModel(String id) {
        super(id);
    }

    @Override
    public void onSelect() {}

    @Override
    public void run() {}
}
