package org.netsim.models;

import lombok.SneakyThrows;

public class ClientServerModel extends Model {

    public static String modelId = "clientserver";
    public Node server;
    public Node client;

    public ClientServerModel() {
        init();
    }

    @Override
    public void init() {
        server = new Node("Server");
        client = new Node("Client1");
        server.connect(client.getIn());
        client.connect(server.getIn());
    }

    @Override
    @SneakyThrows
    public void init(Class<?> userNode) {
        server = (Node) userNode.getDeclaredConstructor(String.class).newInstance("Server");
        client = (Node) userNode.getDeclaredConstructor(String.class).newInstance("Client1");
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

}
