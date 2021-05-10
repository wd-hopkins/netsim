package org.netsim.models;

import org.netsim.cli.Option;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class ByzantineFaultModel extends Model {

    public static String modelId = "ByzantineFault";
    private final char[] chars;
    private final SecureRandom random;
    private Set<Node> faultyNodes;
    @Option(name = "fault_prob",
            description = "Probability of a node becoming faulty.")
    private float faultProb;
    @Option(name = "random_str_len",
            description = "Length of randomly generated message.")
    private int randLen;
    @Option(name = "max_faulty",
            description = "Maximum number of faulty nodes allowed in the system.")
    private int numFaulty;

    public ByzantineFaultModel() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digits = "0123456789";
        this.chars = (upper + lower + digits).toCharArray();
        this.random = new SecureRandom();
        this.faultProb = 0.05f;
        this.randLen = 64;
        this.numFaulty = 1;
    }

    @Override
    public void start() {
        this.faultyNodes = new HashSet<>();
        while (true) {
            if (random.nextFloat() * 100f < faultProb) {
                Node faultyNode = this.nodes.get(random.nextInt(this.nodes.size()));
                if (faultyNodes.size() == numFaulty && !faultyNodes.contains(faultyNode)) {
                    continue;
                }
                faultyNodes.add(faultyNode);
                switch (random.nextInt(4)) {
                    case 0:
                        breakLink(faultyNode);
                        break;
                    case 1:
                        delayLink(faultyNode);
                        break;
                    case 2:
                        sendArbitraryMessage(faultyNode);
                        break;
                    case 3:
                        sendGlobalMessage(faultyNode);
                        break;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    private void breakLink(Node node) {
        int gate = random.nextInt(node.in.size());
        node.in.get(gate).pause();
        System.out.printf("[%s][%s] Broken Link.\n", node.name, node.in.get(gate).getName());
    }

    private void delayLink(Node node) {
        int gate = random.nextInt(node.out.size());
        node.out.get(gate).setWaitingDelay(1000);
        System.out.printf("[%s][%s] Link Delayed.\n", node.name, node.in.get(gate).getName());
    }

    private String randomString() {
        char[] randomStr = new char[randLen];
        for (int i = 0; i < randLen; i++)
            randomStr[i] = chars[random.nextInt(chars.length)];
        return new String(randomStr);
    }

    private void sendArbitraryMessage(Node node) {
        int gateIndex = random.nextInt(node.out.size());
        OutputGate gate = node.out.get(gateIndex);
        while (true) {
            ScheduledFuture<?> queuedEvent = gate.getLatestEvent();
            if (queuedEvent == null || queuedEvent.isDone() || queuedEvent.cancel(true)) {
                break;
            }
        }
        node.send(randomString(), gate);
    }

    private void sendGlobalMessage(Node node) {
        node.send("A global message triggered by an arbitrary fault.");
    }

    public String toString() {
        return modelId;
    }
}
