import org.netsim.models.Node;

import java.security.SecureRandom;
import java.time.LocalTime;

public class Broadcaster extends Node {
    
    private boolean accepted;
    private int maxFaultyNodes;
    private int numEcho;
    private int numNodes;
    private int numReady;
    private int step;

    public Broadcaster(String name) {
        super(name);
        this.accepted = false;
        this.maxFaultyNodes = 1;
        this.numEcho = 0;
        this.numNodes = 6;
        this.numReady = 0;
        this.step = 0;
    }

    @Override
    public void init() {
        String[] headsOrTails = {"Heads", "Tails"};
        int index = new SecureRandom().nextInt(headsOrTails.length);
        send(headsOrTails[index] + "|initial");
        step++;
    }

    @Override
    public void onReceive(Object message) {
        String[] content = ((String) message).split("\\|");
        switch (content[1]) {
            case "initial":
                send(content[0] + "|echo");
                step++;
                return;
            case "echo":
                numEcho++;
                break;
            case "ready":
                numReady++;
                break;
        }
        
        if (step == 1) {
            if (numEcho >= (numNodes + maxFaultyNodes)/2 || numReady == maxFaultyNodes + 1) {
                send(content[0] + "|echo");
                step++;
                return;
            }
        }
        
        if (step == 2) {
            if (numEcho >= (numNodes + maxFaultyNodes)/2 || numReady == maxFaultyNodes + 1) {
                send(content[0] + "|ready");
                step++;
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