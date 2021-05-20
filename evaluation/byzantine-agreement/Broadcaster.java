import org.netsim.models.Node;

import java.security.SecureRandom;
import java.time.LocalTime;

public class Broadcaster extends Node {

    private final int maxFaultyNodes;
    private final int numNodes;
    private boolean accepted;
    private int numEcho;
    private int numReady;
    private int step;

    public Broadcaster(String name) {
        super(name);
        this.maxFaultyNodes = 2;
        this.numNodes = 7;
        this.accepted = false;
        this.numEcho = 0;
        this.numReady = 0;
        this.step = 1;
    }

    @Override
    public void init() {
        if (this.name.equals("broadcaster")) {
            String[] headsOrTails = {"Heads", "Tails"};
            int index = new SecureRandom().nextInt(headsOrTails.length);
            send(headsOrTails[index] + "|initial", 2000);
        }
    }

    @Override
    public synchronized void onReceive(Object message) {
        //System.out.printf("[%s][%s] Received: %s\n", LocalTime.now(), this.name, message);
        String[] content = ((String) message).split("\\|");
        switch (content[1]) {
            case "initial":
                send(content[0] + "|echo", 2000);
                step = 2;
                return;
            case "echo":
                numEcho++;
                break;
            case "ready":
                numReady++;
                break;
        }

        if (step == 1) {
            if (numEcho >= (numNodes + maxFaultyNodes) / 2 || numReady == maxFaultyNodes + 1) {
                send(content[0] + "|echo", 2000);
                step = 2;
                return;
            }
        }

        if (step == 2) {
            if (numEcho >= (numNodes + maxFaultyNodes) / 2 || numReady == maxFaultyNodes + 1) {
                send(content[0] + "|ready", 2000);
                step = 3;
                return;
            }
        }

        if (step == 3) {
            if (numReady == 2 * maxFaultyNodes + 1 && !accepted) {
                System.out.printf("[%s][%s] Value accepted: %s\n", LocalTime.now(), this.name, content[0]);
                accepted = true;
            }
        }
    }
}