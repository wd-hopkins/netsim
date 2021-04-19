package org.netsim.models;

import lombok.Getter;
import lombok.SneakyThrows;
import org.netsim.cli.Option;
import org.netsim.util.ClassUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Model {
    private static final @Getter Set<Class<? extends Model>> extendingClasses;
    public static String modelId = "Choose a model";
    protected List<Node> nodes = new ArrayList<>();
    protected @Getter Thread thread;
    
    @Option(name = "mu",
            description = "Mean of distribution of message delays.")
    public double mu;
    
    @Option(name = "lambda",
            description = "Scale of distribution of message delays.")
    public double lambda;

    static {
        extendingClasses = ClassUtil.collectExtendingClasses(Model.class, "org.netsim.models");
        extendingClasses.remove(EmptyModel.class);
    }

    public Model() {
        this.mu = 0.05;
        this.lambda = 1;
    }

    public abstract void start();

    @SneakyThrows
    public static Model getExtendingClassById(String id) {
        Model m = null;
        for (Class<? extends Model> model : extendingClasses) {
            String modelId = (String) model.getField("modelId").get(null);
            if (id.equals(modelId) || id.equals(modelId.toLowerCase())) {
                m = model.getDeclaredConstructor().newInstance();
                break;
            }
        }
        return m;
    }

    private void applyConnections(Map<String, String> connections) {
        connections.forEach((k, v) -> {
            String[] leftSide = k.split("\\.");
            String[] rightSide = v.split("(\\.|->)");

            Node out = getNodeByName(leftSide[0]);
            Node in = getNodeByName(rightSide[0].trim());
            InputGate inGate = in.getInputGateByName(rightSide[1].trim());
            out.connect(leftSide[1], inGate);

            if (rightSide.length > 3) {
                throw new IllegalArgumentException("Unexpected number of options.");
            } else if (rightSide.length == 3) {
                out.setMaxTransmissionDelay(leftSide[1], Long.parseLong(rightSide[2].trim()));
            }
        });
    }

    public void init(List<Node> nodes, Map<String, String> connections) {
        this.nodes = nodes;
        this.applyConnections(connections);
    }

    public void interrupt() {
        if (thread != null) {
            thread.interrupt();
        }
    }

    public Node getNodeByName(String name) {
        for (Node node : nodes) {
            if (node.name.equals(name)) {
                return node;
            }
        }
        throw new IllegalArgumentException("Node not defined: " + name);
    }
    
    public void run() {
        this.nodes.forEach(x -> x.setDist(this.mu, this.lambda));
        this.start();
    }

    @Override
    public String toString() {
        return modelId;
    }
}
