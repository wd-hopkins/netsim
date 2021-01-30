package org.netsim;

import lombok.Getter;
import lombok.Setter;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;

public class ModelRunner {

    @Getter @Setter
    private Model selectedModel;

    public ModelRunner() {
        selectedModel = new EmptyModel();
    }

    public void run() {
        this.selectedModel.run();
    }
}
