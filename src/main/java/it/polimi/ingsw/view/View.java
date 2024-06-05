package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;


public abstract class View {
    protected Scene currentScene;
    protected final Map<SceneID, Scene> sceneIDMap;

    protected View(Scene currentScene){
        this.currentScene = currentScene;
        sceneIDMap = new Hashtable<>();
    }

    public abstract void setScene(SceneID sceneID) throws IllegalArgumentException;
    public abstract void update(SceneID sceneID, String description);
    public abstract void showError(String errorMsg);
    public abstract void showNotification(String notification);
    public abstract void showChatMessage(String msg);
    public abstract void notifyTimeout();
    public abstract void run() throws RemoteException, TimeoutException, DisconnectException;
}
