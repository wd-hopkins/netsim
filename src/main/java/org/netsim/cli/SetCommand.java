package org.netsim.cli;

import lombok.SneakyThrows;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;

import java.lang.reflect.Field;

@Command(name = "set",
         description = "Set model options")
public class SetCommand implements Runnable {

    @Parameters
    private String option;

    @Parameters
    private int value;

    @SneakyThrows
    @Override
    public void run() { //TODO: set value based on type
        Model selectedModel = CommandShell.getRunner().getSelectedModel();
        Class<? extends Model> clazz = selectedModel.getClass();
        if (clazz == EmptyModel.class) {
            CommandShell.printErr("Select a model first");
        }

        for (Field field: clazz.getDeclaredFields()) {
            if (field.getName().equals(option) && field.isAnnotationPresent(Option.class)) {
                field.setAccessible(true);
                field.set(selectedModel, value);
            }
        }
    }
}
