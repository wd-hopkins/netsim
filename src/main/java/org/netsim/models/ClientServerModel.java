package org.netsim.models;

public class ClientServerModel extends Model {

    public ClientServerModel() {
        super("Client - Server Model");
    }

    @Override
    public void onSelect() {
        System.out.println(this.toString());
    }

    @Override
    public void run() {
        System.out.println("Client Server");
    }
}
