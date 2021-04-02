package org.netsim.models;

import org.netsim.cli.Option;

import java.util.Random;

public class CrashModel extends Model {

    public static String modelId = "Crash";
    @Option(name = "fault_prob",
            description = "Probability that a node will fail each second.")
    private float faultProb;

    public CrashModel() {
        this.faultProb = 0;
    }

    @Override
    public void run() {
        if (faultProb > 100) {
            System.out.println("Probability should be in the range of 0-100.");
            return;
        }

        this.nodes.get(0).init();
        while (true) {
            if (new Random().nextFloat() * 100f <= faultProb) {
                this.nodes.get(new Random().nextInt(this.nodes.size()-1)).halt();
                System.out.println("Oops, a node has crashed");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public String toString() {
        return modelId;
    }
}