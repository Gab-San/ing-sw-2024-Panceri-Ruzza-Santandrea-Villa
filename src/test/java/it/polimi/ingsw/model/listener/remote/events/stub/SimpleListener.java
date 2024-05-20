package it.polimi.ingsw.model.listener.remote.events.stub;

import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.remote.NetworkEvent;

import java.rmi.RemoteException;

public class SimpleListener implements GameListener {
    private final StubClient client;
    public SimpleListener(StubClient client){
        this.client = client;
    }
    @Override
    public void listen(GameEvent event) throws ListenException {
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
