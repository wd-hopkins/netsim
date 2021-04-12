package org.netsim.models;

import java.util.Random;

public class ByzantineFaultModel extends Model {
    
    public static String modelId = "ByzantineFault";
    
    public ByzantineFaultModel() {
    
    }
    
    @Override
    public void run() {
    
    }
    
    private void sendArbitraryMessage(Node node) {
    
    }
    
    private void breakLink(Node node) {
        int gate = new Random().nextInt(node.in.size());
        node.in.get(gate).pause();
    }
    
    private void delayLink(Node node) {
        int gate = new Random().nextInt(node.in.size());
        node.out.get(gate).setWaitingDelay(1000);
    }
    
    public String toString() {
        return modelId;
    }
}
