import org.netsim.models.Node;

import java.security.SecureRandom;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Broadcaster extends Node {

    private final int maxFaultyNodes;
    private final int numNodes;
    private volatile boolean accepted;
    private AtomicInteger numEcho;
    private AtomicInteger numReady;
    private AtomicInteger step;

    public Broadcaster(String name) {
        super(name);
        this.maxFaultyNodes = 1;
        this.numNodes = 2;
        this.accepted = false;
        this.numEcho = new AtomicInteger();
        this.numReady = new AtomicInteger();
        this.step = new AtomicInteger();
    }

    @Override
    public void init() {
        String[] headsOrTails = {"Heads", "Tails"};
        int index = new SecureRandom().nextInt(headsOrTails.length);
        step.set(1);
        send(headsOrTails[index] + "|initial");
    }

    @Override
    public synchronized void onReceive(Object message) {
        String[] content = ((String) message).split("\\|");
        switch (content[1]) {
            case "initial":
                //System.out.printf("[%s][%s] Sending echo\n", LocalTime.now(), this.name);
                send(content[0] + "|echo");
                step.set(2);
                return;
            case "echo":
                numEcho.getAndIncrement();
                break;
            case "ready":
                numReady.getAndIncrement();
                break;
        }

        if (step.get() == 1) {
            if (numEcho.get() >= (numNodes + maxFaultyNodes) / 2 || numReady.get() == maxFaultyNodes + 1) {
                //System.out.printf("[%s][%s] Sending echo\n", LocalTime.now(), this.name);
                send(content[0] + "|echo");
                step.set(2);
                return;
            }
        }

        if (step.get() == 2) {
            if (numEcho.get() >= (numNodes + maxFaultyNodes) / 2 || numReady.get() == maxFaultyNodes + 1) {
                //System.out.printf("[%s][%s] Sending ready\n", LocalTime.now(), this.name);
                send(content[0] + "|ready");
                step.set(3);
                return;
            }
        }

        if (step.get() == 3) {
            if (numReady.get() == 2 * maxFaultyNodes + 1 && !accepted) {
                System.out.printf("[%s][%s] Value accepted: %s\n", LocalTime.now(), this.name, content[0]);
                accepted = true;
            }
        }
    }
}