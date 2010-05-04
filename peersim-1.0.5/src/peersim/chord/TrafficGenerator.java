/**
 * 
 */
package peersim.chord;

import peersim.core.*;
import peersim.config.Configuration;
import peersim.edsim.EDSimulator;

/**
 * We need now to istantiate a control subclass that will generate lookup
 * messages (source and destination are randomly choosen) every predefined
 * step of time.
 *
 * TODO:
 * Daqui partem os pedidos. Deve ser chamado por algum tipo de Schedule rou assim.
 * E' invocado pelo metodo {@link peersim.core.Control#execute()}
 * Mas...
 *
 *
 * @author Andrea
 * 
 */
public class TrafficGenerator implements Control {

    private static final String PAR_PROT = "protocol";
    private final int pid;

    /**
     *
     */
    public TrafficGenerator(String prefix) {
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    /*
     * (non-Javadoc)
     *
     * @see peersim.core.Control#execute()
     */
    public boolean execute() {
        int size = Network.size();
        Node sender, target;
        int i = 0;
        do {
            i++;
            sender = Network.get(CommonState.r.nextInt(size));
            target = Network.get(CommonState.r.nextInt(size));
        } while (sender == null || sender.isUp() == false || target == null
                || target.isUp() == false);
        LookUpMessage message = new LookUpMessage(sender,
                ((ChordProtocol) target.getProtocol(pid)).chordId);
        EDSimulator.add(10, message, sender, pid);
        return false;
    }
}
