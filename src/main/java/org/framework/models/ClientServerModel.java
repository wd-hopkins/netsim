package org.framework.models;

public class ClientServerModel extends Model {

    public ClientServerModel() {
        super("Client - Server Model");
    }

    @Override
    public void onSelect() {
        System.out.println(this.toString());
    }
}
