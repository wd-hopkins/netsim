import org.netsim.models.Node;

import java.time.LocalTime;

public class Broadcaster extends Node {

    public Broadcaster(String name) {
        super(name);
    }

    @Override
    public void init() {
        send("Hello!");
    }

    @Override
    public void onReceive(Object message) {
        System.out.printf("[%s][%s] Received: %s\n", LocalTime.now(), this.name, message);
    }
}