package it.polimi.ingsw.listener.remote;

import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.listener.GameListener;
import it.polimi.ingsw.listener.remote.events.NetworkEvent;
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
    public RemoteHandler() {
        this.playerClients = new Hashtable<>();
        eventRecord = new NetworkEventRecord();
        updateThreadPool = Executors.newCachedThreadPool();
    }

    public void addClient(String nickname, VirtualClient client){
        playerClients.put(nickname, client);
        eventRecord.subscribeClient(client);
        submitUpdates(nickname);
    }
    public void removeClient(String nickname){
        playerClients.remove(nickname);
        eventRecord.unlistClient(playerClients.get(nickname));
        eventRecord.removeAllTasks(playerClients.get(nickname));
    }
    @Override
    public void listen(GameEvent event) throws RemoteException {
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
