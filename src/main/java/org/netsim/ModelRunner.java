package org.netsim;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;
import org.netsim.models.Node;
import org.netsim.util.ClassLoaderUtil;

import java.io.File;

public class ModelRunner {

    private @Getter @Setter File workingDirectory = new File(System.getProperty("user.dir"));
    private @Getter @Setter Model selectedModel;

    public ModelRunner() {
        selectedModel = new EmptyModel();
    }

    @SneakyThrows
    public void run() {
        if (ClassLoaderUtil.compileJavaClass(new File(workingDirectory, "UserImpl.java"))) {
            Class<?> userImpl = ClassLoaderUtil.loadClass(workingDirectory, "UserImpl");
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
