package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface.
 * Sent if a place starting card action is issued.
 */
public class PlaceStartingCardMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 8888L;
    private final String nickname;
    private final boolean placeOnFront;

    /**
     * Constructs the place starting message.
     * @param nickname unique id of the user
     * @param placeOnFront true if the card must face upwards, false otherwise
     */
    public PlaceStartingCardMessage(String nickname, boolean placeOnFront) {
        this.nickname = nickname;
        this.placeOnFront = placeOnFront;
    }


    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.placeStartCard(nickname, virtualClient, placeOnFront);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
