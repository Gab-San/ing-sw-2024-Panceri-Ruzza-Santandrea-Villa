package it.polimi.ingsw.listener.remote;

import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.listener.remote.events.PingEvent;
import it.polimi.ingsw.model.exceptions.ListenException;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RemoteHandler implements GameListener{
    private final Map<String, VirtualClient> playerClients;
    private final NetworkEventRecord eventRecord;
    private final ExecutorService updateThreadPool;
    private static RemoteHandler instance;
    private RemoteHandler() {
        this.playerClients = new Hashtable<>();
        eventRecord = new NetworkEventRecord();
        updateThreadPool = Executors.newCachedThreadPool();
    }

    public synchronized static RemoteHandler getInstance(){
        if(instance == null) instance = new RemoteHandler();
        return instance;
    }

    public void addClient(String nickname, VirtualClient client){
        //Compiles the map
        playerClients.put(nickname, client);
        //Associates with an iterator
        eventRecord.subscribeClient(client);
        //Send updates until disconnection
        submitUpdates(nickname);
    }
    public void removeClient(String nickname){
        VirtualClient virtualClient = playerClients.get(nickname);
        eventRecord.unlistClient(virtualClient);
        eventRecord.forceRemoveTask(virtualClient);
        playerClients.remove(nickname);
    }
    @Override
    public synchronized void listen(GameEvent event) throws ListenException {
        if(!(event instanceof NetworkEvent)){
            return;
        }
        if(event instanceof PingEvent pingEvent){
            VirtualClient client = playerClients.get(pingEvent.getNickname());
            try {
                client.ping();
            } catch (RemoteException e) {
                throw new ListenException(e);
            }
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
