package it.polimi.ingsw.listener.remote;

import it.polimi.ingsw.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.util.*;

public class NetworkEventRecord {
    private final List<NetworkEvent> events;
    private final List<UpdateTask> executingTasks;
    private final Map<VirtualClient, ListIterator<NetworkEvent>> clientUpdateStatus;

    public NetworkEventRecord() {
        this.events = new LinkedList<>();
        this.clientUpdateStatus = new Hashtable<>();
        this.executingTasks = new LinkedList<>();
    }

    public synchronized void addEvent(NetworkEvent networkEvent){
            events.add(networkEvent);
            this.notifyAll();
    }

    public synchronized void subscribeClient(VirtualClient virtualClient){
        ListIterator<NetworkEvent> updateStatus = events.listIterator();
        clientUpdateStatus.put(virtualClient, updateStatus);
    }

    public synchronized void unlistClient(VirtualClient virtualClient){
        clientUpdateStatus.remove(virtualClient);
    }

    public synchronized boolean hasNext(VirtualClient virtualClient){
        ListIterator<NetworkEvent> updateStatus = clientUpdateStatus.get(virtualClient);
        return updateStatus.hasNext();
    }

    public synchronized NetworkEvent getNext(VirtualClient virtualClient){
        ListIterator<NetworkEvent> updateStatus = clientUpdateStatus.get(virtualClient);
        return updateStatus.next();
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

    public void removeAllTasks(VirtualClient client){
        synchronized (executingTasks){
            executingTasks.stream().filter(e-> e.getTaskClient().equals(client)).
                    forEach(UpdateTask::killTask);

            executingTasks.removeAll(executingTasks.parallelStream().
                    filter(e -> !e.getTaskClient().equals(client)).toList());
        }
    }

}
