package org.netsim;

import javassist.compiler.CompileError;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;
import org.netsim.models.Node;
import org.netsim.util.ClassUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ModelRunner {

    private @Getter ScheduledThreadPoolExecutor threadPool;
    private @Getter @Setter File workingDirectory = new File(System.getProperty("user.dir"));
    private @Getter @Setter Model selectedModel;

    public ModelRunner() {
        selectedModel = new EmptyModel();
    }

    @SneakyThrows
    public void run() {
        System.out.println(new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT)
                .append("Beginning simulation of model: ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                .append(this.selectedModel.toString())
                .toAnsi()
        );

        //Check for a config file
        NetworkConfig config = null;
        try {
            config = NetworkConfig.getConfig(new File(workingDirectory, workingDirectory.getName() + ".yaml"));
        } catch (IOException e) {
            System.err.println("Configuration file not found.");
        }

        if (config != null) {
            //Retrieve config objects
            Map<File, NetworkConfig.Gate> configTypes = config.validateTypes(workingDirectory);
            Map<String, String> configNodes = config.getNodes();
            Map<String, String> configConnections = config.getConnections();
            this.threadPool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(configNodes.size());

            //Compile the classes under the types heading
            Map<Class<?>, NetworkConfig.Gate> classes = new HashMap<>();
            if (ClassUtil.compileJavaClass(configTypes.keySet().toArray(new File[0]))) {
                configTypes.forEach((k, v) -> {
                    Class<?> userImpl = ClassUtil.loadClass(workingDirectory, k.getName().split("\\.")[0]);
                    //Check if the class extends Node
                    if (!Node.class.isAssignableFrom(userImpl)) {
                        throw new ClassFormatError(String.format("Error: %s does not extend Node.\n", userImpl.getName()));
                    }
                    classes.put(userImpl, v);
                });
            } else {
                throw new CompileError("There was an error during compilation.");
            }

            //Construct the node types with their respective gates
            List<Node> nodes = new ArrayList<>();
            configNodes.forEach((name, type) -> classes.forEach((clazz, gates) -> {
                if (clazz.getName().equals(type)) {
                    Node n = (Node) ClassUtil.instantiate(clazz, name);
                    n.createInGates(gates.getInput());
                    n.createOutGates(gates.getOutput());
                    nodes.add(n);
                }
            }));
            if (nodes.size() != configNodes.size()) {
                throw new Exception("Could not create one or more nodes. Check your config file.");
            }
            //Give to the model to create the connections
            this.selectedModel.init(nodes, configConnections);
        } else {
            this.threadPool = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
            this.selectedModel.init(Collections.singletonList(new Node("Node")), Collections.singletonMap("Node.out","Node.in"));
        }

        this.selectedModel.start();
        //TODO: Keep track of scheduled events in order to determine if the simulation has concluded
        //Thread.sleep(1000);
        System.out.println("End of simulation");
        //threadPool.shutdown();
    }
}
