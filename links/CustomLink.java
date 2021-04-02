import org.netsim.models.Node;

import java.time.LocalTime;

public class CustomLink extends Node {

    public CustomLink(String name) {
        super(name);
    }

    @Override
    public void init() {
        send("Hello.", "out");
    }

    @Override
    public void onReceive(Object message) {
        System.out.printf("[%s][%s] Received: %s\n", LocalTime.now(), this.name, message);
        send(message, "out");
    }
}