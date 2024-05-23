package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.tui.TUI_Scene;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;


public abstract class View {
    protected final CommandPassthrough serverProxy;
    protected Scene currentScene;
    protected final Map<SceneID, Scene> sceneIDMap;

    protected View(CommandPassthrough serverProxy, Scene currentScene){
        this.currentScene = currentScene;
        sceneIDMap = new Hashtable<>();
        this.serverProxy = serverProxy;
    }

    public abstract void setScene(SceneID sceneID) throws IllegalArgumentException;
    public abstract void update(SceneID sceneID, String description);
    public abstract void showError(String errorMsg);
    public abstract void showNotification(String notification);
    public abstract void run() throws RemoteException;
}
