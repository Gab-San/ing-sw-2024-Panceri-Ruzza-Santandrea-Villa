package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.CommandPassthrough;
import it.polimi.ingsw.view.ModelUpdater;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.tui.scenes.PrintNicknameSelectUI;

import java.rmi.RemoteException;
import java.util.function.Consumer;

public class GUI extends View {

    public GUI(CommandPassthrough serverProxy, Consumer<ModelUpdater> setClientModelUpdater){
        super(new PrintNicknameSelectUI()); // change this scene to GUI scene
        //TODO: make GUI
    }

    @Override
    public void setScene(SceneID sceneID) throws IllegalArgumentException {

    }

    @Override
    public void update(SceneID sceneID, String description) {

    }

    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void showNotification(String notification) {

    }

    @Override
    public void showChatMessage(String msg) {

    }

    @Override
    public void notifyTimeout() {

    }

    @Override
    public void run() throws RemoteException {
        // Avvia la schermata grafica
        System.out.println("Schermata grafica avviata...");
        // Esegui il codice per avviare l'interfaccia grafica
        // Esempio: launchGUI();
    }
}