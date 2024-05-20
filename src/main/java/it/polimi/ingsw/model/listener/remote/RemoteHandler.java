package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.network.VirtualClient;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RemoteHandler implements GameListener{
    private final Map<String, VirtualClient> playerClients;
    private final NetworkEventRecord eventRecord;
    private final ExecutorService updateThreadPool;

    public RemoteHandler() {
        this.playerClients = new Hashtable<>();
        eventRecord = new NetworkEventRecord();
        updateThreadPool = Executors.newCachedThreadPool();
    }


    public synchronized void addClient(String nickname, VirtualClient client){
        if(playerClients.containsValue(client)){
            //TODO decide if to return an exception
            System.err.println("Client already connected");
            return;
        }

        //Compiles the map
        playerClients.put(nickname, client);
        //Associates with an iterator
        eventRecord.subscribeClient(client);
        //Send updates until disconnection
        submitUpdates(nickname);
    }
    public synchronized void removeClient(String nickname){
        if(!playerClients.containsKey(nickname))
            return;
        VirtualClient virtualClient = playerClients.get(nickname);
        eventRecord.forceRemoveTask(virtualClient);
        eventRecord.unlistClient(virtualClient);
        playerClients.remove(nickname);
    }
    @Override
    public synchronized void listen(GameEvent event) throws ListenException {
        if(!(event instanceof NetworkEvent)){
            return;
        }
        eventRecord.addEvent((NetworkEvent) event);
    }

    private void submitUpdates(String nickname){
        UpdateTask updateTask = new UpdateTask(playerClients.get(nickname), eventRecord);
        Future<?> taskFuture = updateThreadPool.submit(updateTask);
        updateTask.setTaskFuture(taskFuture);
        eventRecord.addRunningTask(updateTask);
    }
}
