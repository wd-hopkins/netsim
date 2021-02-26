package org.netsim.cli;

import lombok.SneakyThrows;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.netsim.models.Model;
import picocli.CommandLine.Command;

@Command(name = "list",
         description = "List all models")
public class ListCommand implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        System.out.println(new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                .append("Available models:")
                .toAnsi()
        );
        for (Class<? extends Model> m : Model.getExtendingClasses()) {
            System.out.println(new AttributedStringBuilder()
                    .append("  ")
                    .style(AttributedStyle.BOLD)
                    .append((String) m.getField("modelId").get(null))
                    .toAnsi()
            );
        }
    }
}
