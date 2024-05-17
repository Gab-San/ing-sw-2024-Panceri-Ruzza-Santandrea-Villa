package it.polimi.ingsw.listener.remote.events.stub;


import com.diogonunes.jcolor.Attribute;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

import static com.diogonunes.jcolor.Ansi.colorize;

public class PuppetClient implements VirtualClient {
    private final StubView view;
    private final String nickname;
    public boolean notificationReceived;

    public PuppetClient(String nickname, StubView view) {
        this.view = view;
        this.nickname = nickname;
    }

    @Override
    public void update(String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }


    @Override
    public synchronized void updatePlayer(String nickname, PlayerColor colour) {
        view.getPlayer(nickname).setColor(colour);
    }

    @Override
    public synchronized void updatePlayer(String nickname, int playerTurn) {
        view.getPlayer(nickname).setTurn(playerTurn);
    }

    @Override
    public synchronized void updatePlayer(String nickname, boolean isConnected) {
        view.getPlayer(nickname).setConnected(isConnected);
    }

    @Override
    public void createPlayer(String nickname, boolean isConnected, int turn, PlayerColor colour) throws RemoteException {
        System.out.println(colorize("Being notified by " + this.nickname + "\nof player creation with:\n" +
                nickname + "\n" +
                isConnected + "\n" +
                turn + "\n" +
                colour, Attribute.MAGENTA_TEXT()));
        notificationReceived = true;
        view.addPlayer(nickname, isConnected, turn, colour);
    }

}
