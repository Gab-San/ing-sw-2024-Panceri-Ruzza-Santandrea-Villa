package it.polimi.ingsw.view;

import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;

import java.rmi.RemoteException;


public interface View {
    void update(SceneID sceneID, DisplayEvent event);
    void showError(String errorMsg);
    void showChatMessage(String msg);
    void notifyTimeout();
    void run() throws RemoteException, TimeoutException, DisconnectException;
}
