package org.netsim;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkConfig {
    private @Getter @Setter List<Type> types;
    private @Getter @Setter Map<String, String> nodes;
    private @Getter @Setter Map<String, String> connections;

    public NetworkConfig() {}

    public NetworkConfig(List<Type> types, Map<String, String> nodes, Map<String, String> connections) {
        this.types = types;
        this.nodes = nodes;
        this.connections = connections;
    }

    public static NetworkConfig getConfig(File file) throws IOException {
        InputStream in = file.toURI().toURL().openStream();
        Yaml yaml = new Yaml(new Constructor(NetworkConfig.class));
        return yaml.load(in);
    }

    Map<File, Gate> validateTypes(File workingDirectory) {
        Map<File, Gate> typeFiles = new HashMap<>();
        for (Type type : types) {
            typeFiles.put(new File(workingDirectory, type.getName() + ".java"), type.getGates());
        }
        return typeFiles;
    }

    public static class Type {
        private @Getter @Setter String name;
        private @Getter @Setter Gate gates;
    }

    public static class Gate {
        private @Getter @Setter List<String> input;
        private @Getter @Setter List<String> output;
    }
}
