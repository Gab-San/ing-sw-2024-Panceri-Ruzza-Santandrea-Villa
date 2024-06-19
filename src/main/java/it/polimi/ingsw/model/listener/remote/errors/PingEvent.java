package it.polimi.ingsw.model.listener.remote.errors;

import it.polimi.ingsw.network.VirtualClient;

import java.rmi.RemoteException;

/**
 * This class is classified as an error event due to its special nature.
 * <p>
 *     A ping event is an event that is thrown and handled without any special
 *     treatment. For this reason its nature is more similar to an error
 *     event that just has to be reported than to an update.
 *     It wouldn't make sense to make it an exceptional event since
 *     there exists a structure that can embed it.
 * </p>
 */
public class PingEvent extends RemoteErrorEvent {
    /**
     * Constructs ping event.
     * @param nickname pinged user id
     */
    public PingEvent(String nickname){
        super(nickname);
    }

    @Override
    public void executeEvent(VirtualClient virtualClient) throws RemoteException {
        virtualClient.ping();
    }
}
