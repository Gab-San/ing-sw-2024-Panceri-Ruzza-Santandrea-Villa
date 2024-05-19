package it.polimi.ingsw.model.listener.remote;


import it.polimi.ingsw.model.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.util.*;


public class NetworkEventRecord {
    private final List<NetworkEvent> eventsHistory;
    private final List<UpdateTask> executingTasks;
    private final Map<VirtualClient, Integer> clientUpdateStatus;

    public NetworkEventRecord() {
        this.eventsHistory = new LinkedList<>();
        this.clientUpdateStatus = new Hashtable<>();
        this.executingTasks = new LinkedList<>();
    }

    public synchronized void addEvent(NetworkEvent networkEvent){
            eventsHistory.add(networkEvent);
            this.notifyAll();
    }

    public synchronized void subscribeClient(VirtualClient virtualClient){
        clientUpdateStatus.put(virtualClient, 0);
    }

    public void unlistClient(VirtualClient virtualClient){
        synchronized (clientUpdateStatus) {
            clientUpdateStatus.remove(virtualClient);
        }
    }

    public List<NetworkEvent> getHistory(VirtualClient virtualClient){
        synchronized (clientUpdateStatus) {
            Integer lastUpdate = clientUpdateStatus.get(virtualClient);
            return new LinkedList<>(eventsHistory.subList(lastUpdate, eventsHistory.size()));
        }
    }

    public void saveLastUpdate(VirtualClient virtualClient, int lastIndex){
        synchronized (clientUpdateStatus){
            Integer updateStatus = clientUpdateStatus.get(virtualClient);
            clientUpdateStatus.put(virtualClient, updateStatus + lastIndex);
        }
    }

    public void addRunningTask(UpdateTask task){
        synchronized (executingTasks) {
            executingTasks.add(task);
        }
    }

    public void removeTask(UpdateTask task){
        synchronized (executingTasks){
            executingTasks.remove(task);
        }
    }

    public void forceRemoveTask(VirtualClient client){
        synchronized (executingTasks){
            UpdateTask task = executingTasks.stream().filter(e-> e.getTaskClient().equals(client)).
                    findFirst().orElse(null);
            if(task != null) {
                task.killTask();
                removeTask(task);
            }
        }
    }


}
