import org.netsim.models.Node;

import java.time.LocalTime;

public class Doubler extends Node {

    public Doubler(String name) {
        super(name);
    }

    @Override
    public void onReceive(Object message) {
        System.out.printf("[%s][%s] Received: %s\n", LocalTime.now(), this.name, message);
        int num = Integer.parseInt(((String) message).split("#")[1]);
        send(String.format("%d doubled is %d", num, num+num), "out");
    }
}