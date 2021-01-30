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
            System.out.println("Unable to select model.");
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
