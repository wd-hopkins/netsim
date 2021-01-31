package org.netsim.models;

public class ClientServerModel extends Model {

    public static String modelId = "clientserver";
    public Node server;
    public Node client;

    public ClientServerModel() {
        server = new Node("Server");
        client = new Node("Client1");
        server.connect(client.getIn());
        client.connect(server.getIn());
    }

    @Override
    public void onSelect() {
        System.out.println(this.toString());
    }

    @Override
    public void run() {
        System.out.printf("Beginning simulation of model %s\n", this);
        client.send();
        System.out.println(server.receive());
        System.out.println("End of simulation");
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
