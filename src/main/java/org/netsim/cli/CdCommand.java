package org.netsim.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "cd",
        description = "Change the current working directory")
public class CdCommand implements Runnable {

    @Parameters
    private String dir;

    @Override
    public void run() {
        CommandShell.changeDir(dir);
    }
}
