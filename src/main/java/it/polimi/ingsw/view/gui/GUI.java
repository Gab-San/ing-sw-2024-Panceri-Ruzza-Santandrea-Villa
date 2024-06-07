package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;

import java.rmi.RemoteException;
import java.util.EventListener;

public class GUI implements View {
    @Override
    public void update(SceneID sceneID, DisplayEvent event) {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void showChatMessage(String msg) {

    }

    @Override
    public void notifyTimeout() {

    }

    @Override
    public void run() throws RemoteException, TimeoutException, DisconnectException {

    }
}
