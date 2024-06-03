package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.remote.errors.RemoteErrorEvent;
import it.polimi.ingsw.network.CentralServer;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteErrorHandler implements GameListener {
    private final Map<String, VirtualClient> playerClients;
    private final ExecutorService errorThreadPool;

    public RemoteErrorHandler() {
        this.playerClients = new Hashtable<>();
        errorThreadPool = Executors.newCachedThreadPool();
    }


    public synchronized void addClient(String nickname, VirtualClient client){
        playerClients.put(nickname, client);
    }

    public synchronized void removeClient(String nickname){
        playerClients.remove(nickname);
    }

    @Override
    public synchronized void listen(GameEvent event) throws ListenException {
        if(!(event instanceof RemoteErrorEvent error)){
            return;
        }
        String addressee = error.getUser();
        if(addressee.equalsIgnoreCase("all")){
            broadcastError(error);
            return;
        }

        VirtualClient client =  playerClients.get(addressee);
        if(client == null) return;
        try {
            error.executeEvent(client);
        } catch (RemoteException e) {
            try {
                CentralServer.getSingleton().disconnect(addressee, client);
            } catch (IllegalArgumentException | IllegalStateException ignore){}
            throw new ListenException();
        }
    }

    private void broadcastError(RemoteErrorEvent errorEvent) throws ListenException {
        for(String username : playerClients.keySet()){
            VirtualClient client = playerClients.get(username);
            errorThreadPool.execute(() ->{
                try{
                    errorEvent.executeEvent(client);
                } catch (RemoteException e){
                    CentralServer.getSingleton().disconnect(username, client);
                    System.err.println("ERROR WHILE TRYING TO REPORT TO " + username);
                }
            });
        }
    }
}
