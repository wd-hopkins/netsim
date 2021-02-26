package org.netsim.models;

import lombok.SneakyThrows;
import org.netsim.util.ClassUtil;

public class ClientServerModel extends Model {

    public static String modelId = "ClientServer";

    public ClientServerModel() {

    }

    @Override
    @SneakyThrows
    public void init(Class<?> nodeImpl) {
        Node server = (Node) ClassUtil.instantiate(nodeImpl, "Server");
        Node client = (Node) ClassUtil.instantiate(nodeImpl, "Client");
        server.connect("out", client.getIn().get(0));
        client.connect("out", server.getIn().get(0));
        nodes.add(server);
        nodes.add(client);
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
