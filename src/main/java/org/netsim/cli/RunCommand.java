package org.netsim.cli;

import picocli.CommandLine.Command;

@Command(name = "run",
         description = "Runs the selected model")
public class RunCommand implements Runnable {

    @Override
    public void run() {
        CommandShell.getRunner().run();
    }
}
