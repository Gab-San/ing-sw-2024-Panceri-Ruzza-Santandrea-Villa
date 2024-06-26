package it.polimi.ingsw.stub;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.remote.NetworkEvent;

import java.rmi.RemoteException;

/**
 * This game listener acts as a simple listener executing notification calls instantly.
 */
public class SimpleListener implements GameListener {
    private final StubClient client;
    public SimpleListener(StubClient client){
        this.client = client;
    }
    @Override
    public void listen(GameEvent event) {
        if(!(event instanceof NetworkEvent networkEvent)){
            return;
        }
        try {
            networkEvent.executeEvent(client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
