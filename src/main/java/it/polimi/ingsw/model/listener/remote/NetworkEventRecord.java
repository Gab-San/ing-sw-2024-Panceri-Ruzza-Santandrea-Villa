package it.polimi.ingsw.model.listener.remote;


import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class implements a record of the happened network events.
 *
 * <p>
 *     It stores the information about the already triggered events into a
 *     history. The client notifiers use this structure to then notify the clients
 *     about the events' history.
 * </p>
 */
public class NetworkEventRecord {
    private final List<UpdateEvent> eventsHistory;
    private final List<UpdateTask> executingTasks;
    private final Map<VirtualClient, Integer> clientUpdateStatus;
    private boolean isSaving;

    /**
     * Default constructor.
     */
    public NetworkEventRecord() {
        this.eventsHistory = new LinkedList<>();
        this.clientUpdateStatus = new Hashtable<>();
        this.executingTasks = new LinkedList<>();
    }

    /**
     * Adds an event to the events' history.
     * @param updateEvent last occurred event
     */
    public synchronized void addEvent(UpdateEvent updateEvent){
        eventsHistory.add(updateEvent);
        this.notifyAll();
    }

    /**
     * Subscribes a client to the events' history.
     * @param virtualClient client that wants to be notified
     */
    public void subscribeClient(VirtualClient virtualClient){
        synchronized (clientUpdateStatus) {
            clientUpdateStatus.put(virtualClient, 0);
        }
    }

    /**
     * Unsubscribes a client to the events' history.
     * @param virtualClient client that is to be unlisted
     */
    public void unlistClient(VirtualClient virtualClient){
        synchronized (clientUpdateStatus) {
            clientUpdateStatus.remove(virtualClient);
        }
    }

    /**
     * Returns the section of history still not transferred to the client.
     * @param virtualClient requesting client
     * @return section of history still to be notified
     */
    public List<UpdateEvent> getHistory(VirtualClient virtualClient){
        Integer lastUpdate;
        synchronized (clientUpdateStatus) {
            lastUpdate = clientUpdateStatus.get(virtualClient);
        }
        synchronized (this) {
            return new LinkedList<>(eventsHistory.subList(lastUpdate, eventsHistory.size()));
        }
    }

    /**
     * Marks the last update received by the specified client.
     * @param virtualClient client which needs to save its progress
     * @param lastIndex last update sent
     */
    public void saveLastUpdate(VirtualClient virtualClient, int lastIndex){
        synchronized (clientUpdateStatus){
            Integer updateStatus = clientUpdateStatus.get(virtualClient);
            clientUpdateStatus.put(virtualClient, updateStatus + lastIndex);
        }
    }

    /**
     * Resets the update mark to the given position into the history.
     * @param virtualClient client which is being processed
     * @param newIndex new history mark
     */
    public void resetLastUpdate(VirtualClient virtualClient, int newIndex){
        synchronized (clientUpdateStatus){
            clientUpdateStatus.put(virtualClient, newIndex);
        }
    }

    /**
     * Adds an updating task to the set of history updating tasks.
     * @param task updating task to be added
     */
    public void addRunningTask(UpdateTask task){
        synchronized (executingTasks) {
            executingTasks.add(task);
        }
    }

    /**
     * Removes an updating task from the set of history updating tasks.
     * @param task updating tasks to be removed
     */
    public void removeTask(UpdateTask task){
        synchronized (executingTasks){
            executingTasks.remove(task);
        }
    }

    /**
     * Force removes the task trying to interrupt its execution.
     * @param client client associated to the updating task
     */
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

    /**
     * Returns true if the specified client has received all the events contained into the history, false otherwise.
     * @param virtualClient analyzed client
     * @return true if the client is up-to-date, false otherwise
     */
    public boolean isUptoDate(VirtualClient virtualClient){
        synchronized (clientUpdateStatus){
             synchronized (this) {
                int lastUpdate = clientUpdateStatus.get(virtualClient);
                return lastUpdate >= eventsHistory.size();
             }
        }
    }

    /**
     * Resets the updates of all clients who aren't up-to-date.
     * @param stateSaveSize size of the squashed save
     */
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

    /**
     * Substitutes events' history with a squashed version.
     * @param updateEventList squashed history
     */
    public synchronized void replaceHistory(List<UpdateEvent> updateEventList){
        eventsHistory.clear();
        eventsHistory.addAll(updateEventList);
        setSaving(false);
    }

    /**
     * Sets the event record saving status.
     * @param on true if the event record is saving, false otherwise
     */
    //isSaving is always synchronized on this
    public synchronized void setSaving(boolean on){
        isSaving = on;
        this.notifyAll();
    }

    //isSaving is always synchronized on this

    /**
     * Returns true if the event record is saving, false otherwise.
     * @return true if the event record is saving, false otherwise
     */
    public synchronized boolean isSaving(){
        return isSaving;
    }
}
