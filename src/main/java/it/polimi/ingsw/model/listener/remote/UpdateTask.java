package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

public class UpdateTask implements Runnable{

     private final VirtualClient client;
     private final NetworkEventRecord eventRecord;
     private Future<?> taskFuture;
     private boolean hasToStop;

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
            List<NetworkEvent> history = eventRecord.getHistory(client);
            ListIterator<NetworkEvent> iterator = history.listIterator();

            synchronized (eventRecord) {
                if (!iterator.hasNext()){
                    try {
                        eventRecord.wait();
                    } catch (InterruptedException ignore) {
                    }
                }
            }

            try {
                while (iterator.hasNext() && !hasToStop) {
                    NetworkEvent updateEvent = iterator.next();
                    updateEvent.executeEvent(client);
                }
                int lastUpdate = iterator.nextIndex();
                eventRecord.saveLastUpdate(client, lastUpdate);
            } catch (RemoteException e) {
                break;
            }
        }
        eventRecord.removeTask(this);
    }
    
    void setTaskFuture(Future<?> taskFuture){
        this.taskFuture = taskFuture;
    }
    void killTask(){
        hasToStop = true;
        taskFuture.cancel(true);
    }

    VirtualClient getTaskClient(){
        return client;
    }

}
