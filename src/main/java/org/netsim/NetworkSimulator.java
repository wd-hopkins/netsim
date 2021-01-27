package org.netsim;

public class NetworkSimulator {

    static boolean runWithGui = true;

    public static void main(String[] args) {
        for (String item: args) {
            switch (item) {
                case "--no-gui":
                    runWithGui = false;
            }
        }
        if (runWithGui) {
            GUIApplication.launch(GUIApplication.class, args);
        }
    }
}
