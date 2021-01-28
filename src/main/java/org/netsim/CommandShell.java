package org.netsim;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CommandShell {

    private final BufferedReader reader;

    public CommandShell() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void run() {
        String line;
        while (!(line = readLine(reader)).equals("exit")) {
            doCommand(line);
        }
        exit();
    }

    private void doCommand(String line) {
        String[] components = line.split(" ");
        switch(components[0]) {
            case "run":
                if (components.length > 1)
                    System.out.println("Too many arguments");
                else
                    System.out.println("To be implemented");
                break;
            case "set":
                System.out.println("To be implemented");
                break;
            default:
                if (!components[0].equals(""))
                    System.out.printf("Command not found: %s\n", components[0]);
        }
    }

    @SneakyThrows
    private String readLine(BufferedReader reader) {
        System.out.print("netsim> ");
        return reader.readLine();
    }

    @SneakyThrows
    private void exit() {
        reader.close();
        System.exit(0);
    }
}
