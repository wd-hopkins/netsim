package org.netsim;

import org.netsim.cli.CommandShell;
import org.netsim.ui.GUIApplication;

public class NetworkSimulator {

    public static boolean runWithGui = true;

    public static void main(String[] args) {
        for (String item: args) {
            switch (item) {
                case "--no-gui":
                    runWithGui = false;
                    continue;
                case "-h":
                    showCliOptions();
                    break;
            }
        }
        if (runWithGui) {
            GUIApplication.launch(GUIApplication.class, args);
        } else {
            CommandShell.run();
        }
    }

    private static void showCliOptions() {
        System.out.println("Run with --no-gui for CLI");
        System.exit(0);
    }
}
