package org.netsim;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;
import org.netsim.models.Node;
import org.netsim.util.ClassUtil;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModelRunner {

    private final @Getter ExecutorService threadPool = Executors.newFixedThreadPool(1);
    private @Getter @Setter File workingDirectory = new File(System.getProperty("user.dir"));
    private @Getter @Setter Model selectedModel;

    public ModelRunner() {
        selectedModel = new EmptyModel();
    }

    @SneakyThrows
    public void run() {
        if (ClassUtil.compileJavaClass(new File(workingDirectory, "UserImpl.java"))) {
            Class<?> userImpl = ClassUtil.loadClass(workingDirectory, "UserImpl");
            if (Node.class.isAssignableFrom(userImpl)) {
                this.selectedModel.init(userImpl);
            } else {
                this.selectedModel.init();
            }
        } else {
            this.selectedModel.init();
        }
        this.selectedModel.run();
    }
}
