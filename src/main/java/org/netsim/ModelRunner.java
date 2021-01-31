package org.netsim;

import lombok.Getter;
import lombok.Setter;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;

public class ModelRunner {

    private @Getter @Setter Model selectedModel;

    public ModelRunner() {
        selectedModel = new EmptyModel();
    }

    public void run() {
        this.selectedModel.run();
    }
}
