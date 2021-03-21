package org.netsim.cli;

import lombok.SneakyThrows;
import picocli.CommandLine.Command;

@Command(name = "pwd",
        description = "Prints the current working directory")
public class PwdCommand implements Runnable {

    @SneakyThrows
    @Override
    public void run() {
        System.out.println(CommandShell.getRunner().getWorkingDirectory().getCanonicalPath());
    }
}
