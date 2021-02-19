import org.netsim.models.Node;

import java.time.LocalTime;

public class UserImpl extends Node {

    private int maxMessages;
    private int currMessage;

    public UserImpl(String name) {
        super(name);
        this.maxMessages = 10;
        this.currMessage = 1;
    }

    @Override
    public void init() {
        send(String.format("Initial Message #%d", currMessage++));
    }

    @Override
    public void onReceive(Object message) {
        System.out.printf("[%s][%s] Received: %s\n", LocalTime.now(), this.name, message);
        if (currMessage <= maxMessages) {
            send(String.format("Message #%d", currMessage++));
        }
    }
}