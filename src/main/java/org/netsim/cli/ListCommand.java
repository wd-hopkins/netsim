package org.netsim.cli;

import lombok.SneakyThrows;
import org.netsim.models.Model;
import picocli.CommandLine.Command;

@Command(name = "list",
         description = "List all models")
public class ListCommand implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        for (Class<? extends Model> m : Model.getExtendingClasses()) {
            System.out.println(m.getField("modelId").get(null));
        }
    }
}
