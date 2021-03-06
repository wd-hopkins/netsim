package org.netsim.cli;

import lombok.SneakyThrows;
import org.apache.commons.lang3.ArrayUtils;
import org.netsim.models.EmptyModel;
import org.netsim.models.Model;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.lang.reflect.Field;
import java.util.Map;

@Command(name = "set",
        description = "Set model options")
public class SetCommand implements Runnable {

    @Parameters(paramLabel = "option=value")
    private Map<String, String> option;

    @SneakyThrows
    @Override
    public void run() {
        Model selectedModel = CommandShell.getRunner().getSelectedModel();
        Class<? extends Model> clazz = selectedModel.getClass();
        if (clazz == EmptyModel.class) {
            CommandShell.printErr("Select a model first");
            return;
        }

        option.forEach((k, v) -> {
            Field field = null;
            for (Field f: ArrayUtils.addAll(clazz.getFields(), clazz.getDeclaredFields())) {
                if (f.isAnnotationPresent(Option.class)) {
                    if (!f.getAnnotation(Option.class).name().equals("")) {
                        if (f.getAnnotation(Option.class).name().equals(k)) {
                            field = f;
                            break;
                        }
                    } else if (f.getName().equals(v)) {
                        field = f;
                        break;
                    }
                }
            }
            if (field != null) {
                field.setAccessible(true);
                String fieldType = field.getType().getName();
                try {
                    switch (fieldType) {
                        case "boolean":
                            field.set(selectedModel, Boolean.parseBoolean(v));
                            break;
                        case "byte":
                            field.set(selectedModel, Byte.parseByte(v));
                            break;
                        case "double":
                            field.set(selectedModel, Double.parseDouble(v));
                            break;
                        case "float":
                            field.set(selectedModel, Float.parseFloat(v));
                            break;
                        case "int":
                            field.set(selectedModel, Integer.parseInt(v));
                            break;
                        case "long":
                            field.set(selectedModel, Long.parseLong(v));
                            break;
                        case "short":
                            field.set(selectedModel, Short.parseShort(v));
                            break;
                        default:
                            field.set(selectedModel, v);
                    }
                } catch(IllegalArgumentException e) {
                    CommandShell.printErr("Invalid type. Option has type: " + fieldType);
                } catch (IllegalAccessException ignored) {}
                field.setAccessible(false);
            } else {
                CommandShell.printErr("No such field: " + k);
            }
        });
    }
}
