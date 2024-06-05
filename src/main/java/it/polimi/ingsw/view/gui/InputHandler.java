package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.ViewController;

import java.rmi.RemoteException;

public class InputHandler {
    private final CommandPassthrough virtualServer;
    private final ViewController viewController;
    public InputHandler(CommandPassthrough virtualServer, ViewController viewController){
        this.virtualServer = virtualServer;
        this.viewController = viewController;
    }
    public void connect(String nickname) throws RemoteException, IllegalStateException {
        virtualServer.connect(nickname);
    }
}
