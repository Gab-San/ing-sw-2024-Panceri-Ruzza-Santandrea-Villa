package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.CommandPassthrough;

import java.rmi.RemoteException;

public class GameInputHandler {
    private final CommandPassthrough serverProxy;
    private final GUI gui;

    public GameInputHandler(CommandPassthrough serverProxy, GUI gui){
        this.serverProxy = serverProxy;
        this.gui = gui;
    }

    public void connect(String nickname) throws RemoteException, IllegalStateException {
        serverProxy.connect(nickname);
    }

    public void notifyDisconnection(){
        System.out.println("NOTIFYING DISCONNECTION");
    }
}
