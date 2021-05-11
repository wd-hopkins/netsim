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
        printHeader("Available Models:");

        for (Class<? extends Model> m : Model.getExtendingClasses()) {
            System.out.println(new AttributedStringBuilder()
                    .append("  ")
                    .style(AttributedStyle.BOLD)
                    .append((String) m.getField("modelId").get(null))
                    .toAnsi()
            );
        }
        if (!Model.getUserModels().isEmpty()) {
            printHeader("\nUser Models:");

            for (Class<? extends Model> m : Model.getUserModels()) {
                System.out.println(new AttributedStringBuilder()
                        .append("  ")
                        .style(AttributedStyle.BOLD)
                        .append((String) m.getField("modelId").get(null))
                        .toAnsi()
                );
            }
        }
    }

    private void printHeader(String message) {
        System.out.println(new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                .append(message)
                .toAnsi()
        );
    }
}
