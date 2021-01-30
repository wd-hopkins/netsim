package org.netsim.models;

public class ClientServerModel extends Model {

    public static String modelId = "clientserver";
    public Node server;
    public Node client;

    public ClientServerModel() {
        server = new Node("Server");
        client = new Node("Client1");
        server.connect(server.getIn(), client.getOut());
        client.connect(client.getIn(), server.getOut());
    }

    @Override
    public void onSelect() {
        System.out.println(this.toString());
    }

    @Override
    public void run() {
        System.out.println("Client Server");
    }

    @Override
    public String toString() {
        return modelId;
    }

    @Override
    public void showOptions() {
        System.out.println("Coming Soon");
    }
}
