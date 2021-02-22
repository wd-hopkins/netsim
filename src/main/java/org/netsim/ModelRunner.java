package org.netsim;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;
import org.netsim.models.Node;
import org.netsim.util.ClassUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModelRunner {

    private @Getter ExecutorService threadPool;
    private @Getter @Setter File workingDirectory = new File(System.getProperty("user.dir"));
    private @Getter @Setter Model selectedModel;

    public ModelRunner() {
        selectedModel = new EmptyModel();
    }

    @SneakyThrows
    public void run() {
        System.out.printf("Beginning simulation of model %s\n", this.selectedModel);

        NetworkConfig config = null;
        try {
            config = NetworkConfig.getConfig(new File(workingDirectory, workingDirectory.getName() + ".yaml"));
        } catch (IOException e) {
            System.err.println("Configuration file not found.");
        }

        Class<?> cls = Node.class;
        if (config != null) {
            Map<File, NetworkConfig.Gate> configTypes = config.validateTypes(workingDirectory);
            List<NetworkConfig.Node> configNodes = config.validateNodes();
            Map<String, String> configConnections = config.getConnections();
            this.threadPool = Executors.newFixedThreadPool(configNodes.size());

            Map<Class<?>, NetworkConfig.Gate> classes = new HashMap<>();
            if (ClassUtil.compileJavaClass(configTypes.keySet().toArray(new File[0]))) {
                configTypes.forEach((k, v) -> {
                    Class<?> userImpl = ClassUtil.loadClass(workingDirectory, k.getName().split("\\.")[0]);
                    if (!Node.class.isAssignableFrom(userImpl)) {
                        throw new ClassFormatError(String.format("Error: %s does not extend Node.\n", userImpl.getName()));
                    }
                    classes.put(userImpl, v);
                });
            }

            List<Node> nodes = new ArrayList<>();
            for (NetworkConfig.Node node : configNodes) {
                classes.forEach((k, v) -> {
                    if (k.getName().equals(node.getType())) {
                        Node n = (Node) ClassUtil.instantiate(k, node.getName());
                        n.createInGates(v.getInput());
                        n.createOutGates(v.getOutput());
                        nodes.add(n);
                    }
                });
            }
            if (nodes.size() != configNodes.size()) {
                throw new Exception("Could not create one or more nodes. Check your config file.");
            }
            this.selectedModel.nodes = nodes;
            this.selectedModel.applyConnections(configConnections);
        } else {
            this.threadPool = Executors.newFixedThreadPool(1);
            this.selectedModel.init();
        }

        this.selectedModel.run();
        //TODO: Keep track of scheduled events in order to determine if the simulation has concluded
        Thread.sleep(1000);
        System.out.println("End of simulation");
        threadPool.shutdown();
    }
}
