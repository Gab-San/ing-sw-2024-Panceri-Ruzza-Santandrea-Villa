package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;


public interface View {
    void update(SceneID sceneID, String description);
    void showError(String errorMsg);
    void showNotification(String notification);
    void showChatMessage(String msg);
    void notifyTimeout();
    void run() throws RemoteException, TimeoutException, DisconnectException;
}
