import org.netsim.models.Node;

import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MILLIS;

public class CustomLink extends Node {

    public CustomLink(String name) {
        super(name);
    }

    @Override
    public void init() {
        send(LocalTime.now(), "out");
    }

    @Override
    public void onReceive(Object message) {
        LocalTime time = (LocalTime) message;
        LocalTime now = LocalTime.now();
        System.out.printf("[%s][%s] Transmission time: %s ms\n", now, this.name, MILLIS.between(time, now));
        send(LocalTime.now(), "out");
    }
}