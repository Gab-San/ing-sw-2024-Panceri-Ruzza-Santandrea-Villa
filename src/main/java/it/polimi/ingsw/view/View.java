package it.polimi.ingsw.view;

import it.polimi.ingsw.view.events.DisplayEvent;
import it.polimi.ingsw.view.exceptions.DisconnectException;
import it.polimi.ingsw.view.exceptions.TimeoutException;

import java.rmi.RemoteException;


public interface View {
    /**
     * Notifies the scene of an event. Creates the OpponentScene if it didn't exist. <br>
     * Events may be treated differently whether the scene with given ID is the currentScene or not. <br>
     * Events notified to a non-existing scene are ignored.
     * @param sceneID scene ID as obtained via SceneID.getXSceneID()
     * @param event an event to notify
     */
    void update(SceneID sceneID, DisplayEvent event);

    /**
     * Displays an error message on the current scene.
     * @param errorMsg the error message.
     */
    void showError(String errorMsg);

    /**
     * Displays a new chat message.
     * @param messenger the author of the message
     * @param msg the message as text
     */
    void showChatMessage(String messenger, String msg);

    /**
     * Displays the last notification.
     * @param notification notification message
     */
    void showNotification(String notification);

    /**
     * Notifies the View of having been forcibly disconnected from
     * the server due to a timeout.
     */
    void notifyTimeout();

    /**
     * Launches the View from nickname selection.
     * @throws RemoteException if a connection error occurs while communicating with the server.
     * @throws TimeoutException if this client is forcibly disconnected from the server due to a timeout.
     * @throws DisconnectException if this client explicitly disconnects from the server
     */
    void run() throws RemoteException, TimeoutException, DisconnectException;
}
