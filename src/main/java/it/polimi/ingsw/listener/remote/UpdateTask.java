package it.polimi.ingsw.listener.remote;

import it.polimi.ingsw.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;
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
            try {
                NetworkEvent updateEvent;
                synchronized (eventRecord) {
                    if (!eventRecord.hasNext(client)) {
                        try {
                            eventRecord.wait();
                        } catch (InterruptedException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    updateEvent = eventRecord.getNext(client);
                }
                try {
                    updateEvent.executeEvent(client);
                } catch (RemoteException e) {
                    break;
                }
            } catch (NullPointerException pointerException){
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
