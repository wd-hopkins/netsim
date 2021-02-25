package org.netsim.models;

import lombok.SneakyThrows;
import org.netsim.util.ClassUtil;

public class ClientServerModel extends Model {

    public ClientServerModel() {
        this.modelId = "clientserver";
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
    public void onSelect() {
        System.out.println(this.toString());
    }

    @Override
    public void run() {
        nodes.get(0).init();
    }

}
