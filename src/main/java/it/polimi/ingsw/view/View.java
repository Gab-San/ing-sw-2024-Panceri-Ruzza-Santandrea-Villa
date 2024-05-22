package it.polimi.ingsw.view;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.tui.TUI_Scene;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;


public abstract class View {
    protected final CommandPassthrough serverProxy;
    protected TUI_Scene currentScene;
    protected final Map<SceneID, TUI_Scene> sceneIDMap;

    protected View(CommandPassthrough serverProxy, TUI_Scene currentScene){
        this.currentScene = currentScene;
        sceneIDMap = new Hashtable<>();
        this.serverProxy = serverProxy;
    }

    public abstract void update(SceneID sceneID, String description);
    public abstract void run() throws RemoteException;
}
