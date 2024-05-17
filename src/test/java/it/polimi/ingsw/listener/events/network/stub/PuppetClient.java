package it.polimi.ingsw.listener.events.network.stub;


import it.polimi.ingsw.listener.events.GameEvent;
import it.polimi.ingsw.listener.events.network.NetworkEvent;
import it.polimi.ingsw.model.enums.PlayerColor;
import it.polimi.ingsw.server.VirtualClient;

import java.rmi.RemoteException;

public class PuppetClient implements VirtualClient {
    private final StubView view;

    public PuppetClient(StubView view) {
        this.view = view;
    }

    @Override
    public void update(String msg) throws RemoteException {

    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public synchronized void updatePlayer(String nickname) {
        view.addPlayer(nickname);
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
    public void listen(GameEvent event) throws RemoteException {
        if(!(event instanceof NetworkEvent command)){
            return;
        }
        command.executeEvent(this);
    }
}
