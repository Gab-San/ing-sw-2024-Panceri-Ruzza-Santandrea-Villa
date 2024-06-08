package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.UIFunctions;
import it.polimi.ingsw.view.ViewController;

import java.rmi.RemoteException;

public class GameInputHandler {
    private final CommandPassthrough serverProxy;
    private final GUI gui;
    private final ViewController controller;
    public GameInputHandler(CommandPassthrough serverProxy, GUI gui, ViewController controller){
        this.serverProxy = serverProxy;
        this.gui = gui;
        this.controller = controller;
    }

    private boolean validateNickname(String nickname){
        return nickname.matches("[^\n ].*[a-zA-Z].*[^\n ]")
                && nickname.length() < Client.MAX_NICKNAME_LENGTH;
    }

    public void connect(String nickname) throws RemoteException, IllegalStateException {
        if (!validateNickname(nickname)) {
            throw new IllegalStateException(UIFunctions.evaluateErrorType(nickname));
        }
        controller.addLocalPlayer(nickname);
        controller.setSelfPlayerArea();
        serverProxy.connect(nickname);
    }

    public void notifyDisconnection(){
        System.out.println("NOTIFYING DISCONNECTION");
    }
}
