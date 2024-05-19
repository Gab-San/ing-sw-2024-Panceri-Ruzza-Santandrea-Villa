package it.polimi.ingsw.model.listener.remote;

import it.polimi.ingsw.model.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

public class UpdateTask implements Runnable{

     private final VirtualClient client;
     private final NetworkEventRecord eventRecord;
     private Future<?> taskFuture;

    public UpdateTask(VirtualClient client, NetworkEventRecord eventRecord) {
        this.client = client;
        this.eventRecord = eventRecord;
    }


    /**
     * Runs this operation.
     */
    @Override
    public void run() {

        while (true) {
            NetworkEvent updateEvent;
            List<NetworkEvent> history = eventRecord.getHistory(client);
            ListIterator<NetworkEvent> iterator = history.listIterator();

            synchronized (eventRecord) {
                if (!iterator.hasNext()){
                    try {
                        eventRecord.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Update task was interrupted");
                    }
                }
            }

            try {
                while (iterator.hasNext()) {
                    updateEvent = iterator.next();
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
       taskFuture.cancel(true);
    }

    VirtualClient getTaskClient(){
        return client;
    }

}
