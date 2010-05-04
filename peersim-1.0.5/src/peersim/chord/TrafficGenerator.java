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
 * TODO
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

    /**
     * TODO
     * Podemos pensar como queremos que os nossos pedidos sejam gerados
     * Isto vai ser substituido pelo sistema do grafo que vai necessitar de
     * effectuar requests e assim...
     *
     * Precisamos de coisas adicionais aqui:
     * Gerar o id a partido do Hash da key que desejamos
     * provavelmente vamos necessitar de varios pedidos...
     *
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
            sender = Network.get(CommonState.r.nextInt(size)); // TODO This probably will not be random...
            target = Network.get(CommonState.r.nextInt(size)); // TODO Replace by hash(key) or something
        } while (sender == null || sender.isUp() == false || target == null
                || target.isUp() == false);

        /**
         * TODO Create LookUpMessages
         *
         *
         */
        LookUpMessage message = new LookUpMessage(sender, ((ChordProtocol) target.getProtocol(pid)).chordId);
        EDSimulator.add(10, message, sender, pid);
        return false;
    }
}
