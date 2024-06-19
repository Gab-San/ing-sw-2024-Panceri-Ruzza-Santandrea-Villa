package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.listener.remote.events.UpdateEvent;
import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

/**
 * This class implements an update task.
 *
 * <p>
 *     An update task is responsible for updating the
 *     clients about the events' history.
 * </p>
 */
public class UpdateTask implements Runnable{

     private final VirtualClient client;
     private final NetworkEventRecord eventRecord;
     private Future<?> taskFuture;
     private boolean hasToStop;

    /**
     * Constructs an update task for the specified client.
     * @param client client to be updated
     * @param eventRecord events' history record
     */
    public UpdateTask(VirtualClient client, NetworkEventRecord eventRecord) {
        this.client = client;
        hasToStop = false;
        this.eventRecord = eventRecord;
    }


    /**
     * Runs this operation.
     */
    @Override
    public void run() {

        while (!hasToStop) {
            synchronized (eventRecord) {
                while (eventRecord.isSaving()) {
                    try {
                        eventRecord.wait();
                    } catch (InterruptedException ignore) {}
                }
            }

            List<UpdateEvent> history = eventRecord.getHistory(client);
            ListIterator<UpdateEvent> iterator = history.listIterator();
            eventRecord.saveLastUpdate(client, history.size());

            synchronized (eventRecord) {
                if (!iterator.hasNext()){
                    try {
                        eventRecord.wait();
                    } catch (InterruptedException ignore) {}
                }
            }

            try {
                while (iterator.hasNext() && !hasToStop) {
                    UpdateEvent updateEvent = iterator.next();
                    updateEvent.executeEvent(client);
                }
            } catch (RemoteException e) {
                break;
            }
        }
        eventRecord.removeTask(this);
    }

    /**
     * Sets the future representing this task.
     * @param taskFuture future representing this task.
     */
    void setTaskFuture(Future<?> taskFuture){
        this.taskFuture = taskFuture;
    }

    /**
     * Forces task interruption.
     */
    void killTask(){
        hasToStop = true;
        taskFuture.cancel(true);
    }

    /**
     * Returns the client handled by this task.
     * @return handled client
     */
    VirtualClient getTaskClient(){
        return client;
    }

}
