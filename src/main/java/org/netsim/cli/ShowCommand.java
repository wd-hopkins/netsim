package org.netsim.cli;

import lombok.SneakyThrows;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.netsim.models.Model;
import picocli.CommandLine.Command;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Command(name = "show",
         description = "Show options for selected model")
public class ShowCommand implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        Model selectedModel = CommandShell.getRunner().getSelectedModel();
        Class<? extends Model> clazz = selectedModel.getClass();
        List<Field> options = new ArrayList<>();

        for (Field field: clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Option.class)) {
                options.add(field);
            }
        }

        System.out.println(new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT)
                .append("Selected model: ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                .append((String) clazz.getField("modelId").get(null))
                .toAnsi()
        );

        if (options.isEmpty()) {
            CommandShell.printErr("No options available.");
            return;
        }

        for (Field field: options) {
            String annotatedName = field.getAnnotation(Option.class).name();
            String name = annotatedName.equals("") ? field.getName() : annotatedName;
            field.setAccessible(true);
            String option = new AttributedStringBuilder()
                    .style(AttributedStyle.BOLD)
                    .append(name)
                    .style(AttributedStyle.DEFAULT)
                    .append(" = ")
                    .append(field.get(selectedModel).toString())
                    .toAnsi();
            System.out.printf("  %-10s   %s\n", option, field.getAnnotation(Option.class).description());
        }
    }
}
