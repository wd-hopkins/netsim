package org.netsim.models;

import lombok.SneakyThrows;
import org.netsim.util.ClassUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Model {
    private static final Set<Class<? extends Model>> extendingClasses = ClassUtil.collectExtendingClasses(Model.class, "org.netsim.models");
    public static String modelId = "Choose a model";
    public List<Node> nodes;

    public Model() {

    }

    @SneakyThrows
    public static Model getExtendingClassById(String id) {
        Model m = null;
        for (Class<? extends Model> model : extendingClasses) {
            if (id.equals(model.getField("modelId").get(null))) {
                m = model.getDeclaredConstructor().newInstance();
                break;
            }
        }
        return m;
    }

    public void init() {
        init(Node.class);
    }

    public abstract void init(Class<?> nodeImpl);

    public abstract void onSelect();

    public abstract void run();

    public void applyConnections(Map<String, String> connections) {
        connections.forEach((k, v) -> {
            String[] leftSide = k.split("\\.");
            String[] rightSide = v.split("\\.");
            Node out = getNodeByName(leftSide[0]);
            Node in = getNodeByName(rightSide[0]);
            if (out != null && in != null) {
                out.connect(leftSide[1], in.getInputGateByName(rightSide[1]));
            } else {
                throw new IllegalArgumentException("Node does not exist: " + ((out == null) ? leftSide[0] : rightSide[0]));
            }
        });
    }

    public Node getNodeByName(String name) {
        for (Node node: nodes) {
            if (node.name.equals(name)) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return modelId;
    }
}
