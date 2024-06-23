package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.listener.GameEvent;
import it.polimi.ingsw.model.listener.GameListener;
import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class handles updates that have to be sent over the network.
 */
public class RemoteHandler implements GameListener{
    private final Map<String, VirtualClient> playerClients;
    private final NetworkEventRecord eventRecord;
    private final ExecutorService updateThreadPool;

    /**
     * Default cosntructor.
     */
    public RemoteHandler() {
        this.playerClients = new Hashtable<>();
        eventRecord = new NetworkEventRecord();
        updateThreadPool = Executors.newCachedThreadPool();
    }

    /**
     * Adds a client to be notified.
     * @param nickname client's user id
     * @param client subscribing client
     */
    public void addClient(String nickname, VirtualClient client){
        if(playerClients.containsValue(client)){
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

    /**
     * Removes client associated with user id.
     * @param nickname user identifier
     */
    public void removeClient(String nickname){
        if(!playerClients.containsKey(nickname))
            return;
        VirtualClient virtualClient = playerClients.get(nickname);
        eventRecord.forceRemoveTask(virtualClient);
        eventRecord.unlistClient(virtualClient);
        playerClients.remove(nickname);
    }

    /**
     * Adds the event to the event history.
     * @param event event triggered by a modification of the subject
     */
    @Override
    public synchronized void listen(GameEvent event) {
        if(!(event instanceof UpdateEvent)){
            return;
        }
        eventRecord.addEvent((UpdateEvent) event);
    }

    /**
     * Starts the updating thread for the specified user.
     * @param nickname user id
     */
    private void submitUpdates(String nickname){
        UpdateTask updateTask = new UpdateTask(playerClients.get(nickname), eventRecord);
        Future<?> taskFuture = updateThreadPool.submit(updateTask);
        updateTask.setTaskFuture(taskFuture);
        eventRecord.addRunningTask(updateTask);
    }

    /**
     * Returns the clients that are being notified.
     * @return clients that are being notified
     */
    public synchronized Map<String, VirtualClient> getClients(){
        return playerClients;
    }

    /**
     * Replaces the old history with a squashed one.
     * @param stateSave squashed history save
     */
    public synchronized void replaceHistory(List<UpdateEvent> stateSave){
        eventRecord.setSaving(true);
        eventRecord.resetUpdates(stateSave.size());
        eventRecord.replaceHistory(stateSave);
    }
}
