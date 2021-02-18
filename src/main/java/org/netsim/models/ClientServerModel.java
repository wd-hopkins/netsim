package org.netsim.models;

import lombok.SneakyThrows;
import org.netsim.util.ClassUtil;

public class ClientServerModel extends Model {

    public static String modelId = "clientserver";
    private Node server;
    private Node client;

    public ClientServerModel() {

    }

    @Override
    @SneakyThrows
    public void init(Class<?> nodeImpl) {
        server = (Node) ClassUtil.instantiate(nodeImpl, "Server");
        client = (Node) ClassUtil.instantiate(nodeImpl, "Client");
        server.connect(client.getIn());
        client.connect(server.getIn());
    }

    @Override
    public void onSelect() {
        System.out.println(this.toString());
    }

    @Override
    public void run() {
        client.init();
    }

    @Override
    public String toString() {
        return modelId;
    }

}
