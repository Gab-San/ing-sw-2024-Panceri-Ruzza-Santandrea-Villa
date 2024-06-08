package it.polimi.ingsw.model.listener.remote;


import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.view.tui.ConsoleTextColors;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class NetworkEventRecord {
    private final List<UpdateEvent> eventsHistory;
    private final List<UpdateTask> executingTasks;
    private final Map<VirtualClient, Integer> clientUpdateStatus;
    private boolean isSaving;

    public NetworkEventRecord() {
        this.eventsHistory = new LinkedList<>();
        this.clientUpdateStatus = new Hashtable<>();
        this.executingTasks = new LinkedList<>();
    }

    public synchronized void addEvent(UpdateEvent updateEvent){
            eventsHistory.add(updateEvent);
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

    public List<UpdateEvent> getHistory(VirtualClient virtualClient){
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
    public void resetLastUpdate(VirtualClient virtualClient, int newIndex){
        synchronized (clientUpdateStatus){
            clientUpdateStatus.put(virtualClient, newIndex);
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

    public boolean isUptoDate(VirtualClient virtualClient){
        synchronized (clientUpdateStatus){
             synchronized (this) {
                int lastUpdate = clientUpdateStatus.get(virtualClient);
                boolean isUptoDate = lastUpdate >= eventsHistory.size();
//                System.err.println(ConsoleTextColors.BLUE_TEXT + virtualClient + " " + isUptoDate +
//                        "\n" + lastUpdate +
//                        ConsoleTextColors.RESET);
                return isUptoDate;
             }
        }
    }

    public void resetUpdates(int stateSaveSize){
        synchronized (clientUpdateStatus) {
            for (VirtualClient virtualClient : clientUpdateStatus.keySet()) {
                if (isUptoDate(virtualClient)) {
                    resetLastUpdate(virtualClient, stateSaveSize);
                } else {
                    resetLastUpdate(virtualClient, 0);
                }
            }
        }
    }

    public synchronized void replaceHistory(List<UpdateEvent> updateEventList){
        eventsHistory.clear();
        eventsHistory.addAll(updateEventList);
        setSaving(false);
    }

    //isSaving is always synchronized on this
    public synchronized void setSaving(boolean on){
        isSaving = on;
        this.notifyAll();
    }

    //isSaving is always synchronized on this
    public synchronized boolean isSaving(){
        return isSaving;
    }
}
