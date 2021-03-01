package org.netsim.models;

public class ClientServerModel extends Model {

    public static String modelId = "ClientServer";

    public ClientServerModel() {

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
