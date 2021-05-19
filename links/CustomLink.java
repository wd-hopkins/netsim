import org.netsim.models.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;

import static java.time.temporal.ChronoUnit.MILLIS;

public class CustomLink extends Node {
    
    private BufferedWriter bw;

    public CustomLink(String name) {
        super(name);
    }

    @Override
    public void init() {
        if (this.name.equals("async")) {
            send(LocalTime.now(), "out");
        }
    }

    @Override
    public synchronized void onReceive(Object message) {
        LocalTime time = (LocalTime) message;
        LocalTime now = LocalTime.now();
        long diff = MILLIS.between(time, now);
        System.out.printf("[%s][%s] Transmission time: %s ms\n", now, this.name, diff);
        try {
            bw = new BufferedWriter(new FileWriter("data", true));
            bw.write(String.valueOf(diff));
            bw.newLine();
            bw.close();
        } catch (IOException ignored) {
            System.out.println("Failure writing to file.");
            return;
        }
        send(LocalTime.now(), "out");
    }
}