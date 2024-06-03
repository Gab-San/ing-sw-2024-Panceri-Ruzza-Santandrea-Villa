package it.polimi.ingsw.network.tcp.message.commands;

import it.polimi.ingsw.network.VirtualClient;
import it.polimi.ingsw.network.VirtualServer;
import it.polimi.ingsw.network.tcp.message.TCPClientMessage;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * This class implements tcp client message interface.
 * Sent if a restart game action is requested.
 */
public class RestartGameMessage implements TCPClientMessage {
    @Serial
    private static final long serialVersionUID = 73L;
    private final String nickname;
    private final int numOfPlayers;

    /**
     * Constructs the restart game message.
     * @param nickname unique id of the user
     * @param numOfPlayers number of players with which restart the game.
     *                     <p>
     *                          If the number is lower than the connected players at the time of the request,
     *                          than this value will be ignored.
     *                     </p>
     */
    public RestartGameMessage(String nickname, int numOfPlayers) {
        this.nickname = nickname;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(VirtualServer virtualServer, VirtualClient virtualClient) throws RemoteException {
        virtualServer.restartGame(nickname, virtualClient, numOfPlayers);
    }

    @Override
    public boolean isCheck() {
        return false;
    }
}
