package org.netsim.cli;

import org.netsim.models.Model;
import picocli.CommandLine.Command;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.Parameters;

@Command(name = "select", mixinStandardHelpOptions = true,
         description = "Select a standard model")
public class SelectCommand implements Runnable {

    @Parameters(converter = ModelConverter.class)
    private Model model;

    @Override
    public void run() {
        if (model == null) {
            CommandShell.printErr("Unable to select model.");
        } else if (model.getClass() == CommandShell.getRunner().getSelectedModel().getClass()){
            CommandShell.printErr("Model already selected.");
        } else {
            CommandShell.getRunner().setSelectedModel(model);
        }
    }

    static class ModelConverter implements ITypeConverter<Model> {

        @Override
        public Model convert(String value) {
            return Model.getExtendingClassById(value);
        }
    }
}
