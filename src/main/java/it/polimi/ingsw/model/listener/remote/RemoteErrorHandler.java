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

/**
 * This class is the handler of errors event that need to be notified on a network.
 */
public class RemoteErrorHandler implements GameListener {
    private final Map<String, VirtualClient> playerClients;
    private final ExecutorService errorThreadPool;

    /**
     * Default constructor.
     */
    public RemoteErrorHandler() {
        this.playerClients = new Hashtable<>();
        errorThreadPool = Executors.newCachedThreadPool();
    }

    /**
     * Adds a client to the set of notified clients.
     * @param nickname client's user's identifier
     * @param client notified client instance
     */
    public void addClient(String nickname, VirtualClient client){
        playerClients.put(nickname, client);
    }

    /**
     * Removes a client from the set of notified clients.
     * @param nickname client's user's identifier
     */
    public void removeClient(String nickname){
        playerClients.remove(nickname);
    }

    /**
     * Notifies the clients of the raised error.
     * @param event event triggered by a modification of the subject
     * @throws ListenException if a client cannot be reached
     */
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
