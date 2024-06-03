package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface.
 * Sent if a set player action is issued.
 */
public class SetNumofPlayerMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 0010L;
    private final String nickname;
    private final int numOfPlayers;

    /**
     * Constructs the set player message.
     * @param nickname unique id of the user
     * @param numOfPlayers number of players to wait before starting the match
     */
    public SetNumofPlayerMessage(String nickname, int numOfPlayers) {
        this.nickname = nickname;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.setNumOfPlayers(nickname,virtualClient,numOfPlayers);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
