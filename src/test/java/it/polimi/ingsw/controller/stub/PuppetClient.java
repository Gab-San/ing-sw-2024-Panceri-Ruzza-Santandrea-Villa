package it.polimi.ingsw.controller.stub;

import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.listener.remote.events.NetworkEvent;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.listener.GameEvent;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetClient implements VirtualClient {
    @Override
    public void update(String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {
        System.out.println(colorize("Pinging client...", Attribute.BLACK_TEXT(), Attribute.WHITE_BACK()));
    }

    @Override
    public void updatePlayer(String nickname, PlayerColor colour) {

    }

    @Override
    public void updatePlayer(String nickname, int playerTurn) {

    }

    @Override
    public void updatePlayer(String nickname, boolean isConnected) {

    }

    @Override
    public void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {

    }
}
