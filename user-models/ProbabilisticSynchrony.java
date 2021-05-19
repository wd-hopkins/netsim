import org.netsim.models.Model;
import org.netsim.models.Node;
import org.netsim.models.OutputGate;
import org.netsim.cli.Option;

import java.security.SecureRandom;
import java.util.concurrent.ScheduledFuture;

public class ProbabilisticSynchrony extends Model {

    public static String modelId = "ProbSynchrony";
    private SecureRandom random;

    @Option(name = "fault_prob",
            description = "Probability that a link will lose a message")
    private double faultProb;

    @Option(name = "max_delay",
            description = "Time (ms) that messages are guaranteed to arrive by.")
    private long delay;

    public ProbabilisticSynchrony() {
        this.random = new SecureRandom();
        this.faultProb = 0;
        this.delay = 0;
    }

    @Override
    public boolean setup() {
        if (faultProb > 100) {
            System.out.println("Probability should be in the range of 0-100.");
            return false;
        }
        this.nodes.forEach(node -> node.setMaxTransmissionDelay(this.delay));
        return true;
    }

    @Override
    public void run() {
        while (true) {
            if (random.nextFloat() * 100f < faultProb) {
                Node randomNode = nodes.get(random.nextInt(nodes.size()));
                OutputGate randomGate = randomNode.getOut().get(random.nextInt(randomNode.getOut().size()));
                while (true) {
                    ScheduledFuture<?> queuedEvent = randomGate.getLatestEvent();
                    if (queuedEvent != null && !queuedEvent.isDone() && queuedEvent.cancel(true)) {
                        System.out.println("Message Deleted");
                        break;
                    }
                }
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