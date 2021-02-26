package org.netsim.cli;

import lombok.Getter;
import org.fusesource.jansi.AnsiConsole;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.Builtins;
import org.jline.console.impl.SystemRegistryImpl;
import org.jline.reader.*;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.netsim.ModelRunner;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.shell.jline3.PicocliCommands;
import picocli.shell.jline3.PicocliCommands.ClearScreen;
import picocli.shell.jline3.PicocliCommands.PicocliCommandsFactory;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.function.Supplier;

public class CommandShell {

    private static final @Getter ModelRunner runner = new ModelRunner();

    public static void run() {
        getWorkingDirectory();
        AnsiConsole.systemInstall();
        try {
            Supplier<Path> workDir = () -> Paths.get(runner.getWorkingDirectory().getAbsolutePath());
            Builtins builtins = new Builtins(workDir, null, null);
            builtins.rename(Builtins.Command.TTOP, "top");
            builtins.alias("zle", "widget");
            builtins.alias("bindkey", "keymap");
            CliCommands commands = new CliCommands();

            PicocliCommandsFactory factory = new PicocliCommandsFactory();
            CommandLine cmd = new CommandLine(commands, factory);
            PicocliCommands picocliCommands = new PicocliCommands(cmd);
            Parser parser = new DefaultParser();
            try (Terminal terminal = TerminalBuilder.builder().build()) {
                SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, null);
                systemRegistry.setCommandRegistries(builtins, picocliCommands);
                systemRegistry.register("help", picocliCommands);

                LineReader reader = LineReaderBuilder.builder()
                        .terminal(terminal)
                        .completer(systemRegistry.completer())
                        .parser(parser)
                        .variable(LineReader.LIST_MAX, 50)   // max tab completion candidates
                        .build();
                builtins.setLineReader(reader);
                factory.setTerminal(terminal);

                String prompt = "netsim> ";
                String line;
                while (true) {
                    try {
                        systemRegistry.cleanUp();
                        line = reader.readLine(prompt, null, (MaskingCallback) null, null);
                        systemRegistry.execute(line);
                    } catch (UserInterruptException ignored) { // When user presses ctrl-c

                    } catch (EndOfFileException e) { // When user presses ctrl-d
                        return;
                    } catch (Exception e) {
                        systemRegistry.trace(e);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            AnsiConsole.systemUninstall();
        }
    }

    private static void getWorkingDirectory() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Choose a working directory (leave blank for current): ");
        String dir = scan.nextLine();
        if (dir.isEmpty()) {
            return;
        }

        if (changeDir(dir)) {
            return;
        }
        getWorkingDirectory();
    }

    static boolean changeDir(String dir) {
        File workingDir = new File(dir);
        try {
            if (workingDir.exists() && workingDir.isDirectory()) {
                runner.setWorkingDirectory(workingDir);
                return true;
            } else {
                System.out.println("Selection Invalid.");
                return false;
            }
        } catch (SecurityException e) {
            System.out.println("Permission Denied.");
            return false;
        }
    }

    static void printErr(String str) {
        System.out.println(new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                .append(str)
                .toAnsi());
    }

    @Command(name = "",
            subcommands = {
                    CdCommand.class,
                    ClearScreen.class,
                    HelpCommand.class,
                    SelectCommand.class,
                    RunCommand.class,
                    ShowCommand.class,
                    SetCommand.class,
                    ListCommand.class
            })
    static class CliCommands implements Runnable {
        PrintWriter out;

        CliCommands() {
        }

        public void run() {
            out.println(new CommandLine(this).getUsageMessage());
        }
    }
}
