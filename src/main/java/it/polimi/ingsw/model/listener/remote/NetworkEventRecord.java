package it.polimi.ingsw.model.listener.remote;


import it.polimi.ingsw.network.VirtualClient;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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

    public void subscribeClient(VirtualClient virtualClient){
        synchronized (clientUpdateStatus) {
            clientUpdateStatus.put(virtualClient, 0);
        }
    }

    public void unlistClient(VirtualClient virtualClient){
        synchronized (clientUpdateStatus) {
            clientUpdateStatus.remove(virtualClient);
        }
    }

    public List<NetworkEvent> getHistory(VirtualClient virtualClient){
        Integer lastUpdate;
        synchronized (clientUpdateStatus) {
            lastUpdate = clientUpdateStatus.get(virtualClient);
        }
        synchronized (this) {
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
