package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

/**
 * This class represents a rmi update.
 * <p>
 *     Displays the message sent by another player
 * </p>
 */
public class DisplayMessageUpdate extends RMIUpdate {

    private final String messenger, message;

    /**
     * Constructs a rmi update.
     * @param modelUpdater instance of the model updater referenced to execute updates
     * @param messenger unique id of the user who sent the message
     * @param message text sent
     */
    public DisplayMessageUpdate(ModelUpdater modelUpdater, String messenger, String message) {
        super(modelUpdater);
        this.messenger = messenger;
        this.message = message;
    }

    /**
     * Updates the view displaying a new chat message.
     */
    @Override
    public void update() {
        if(modelUpdater != null)
            modelUpdater.displayMessage(messenger, message);
        else
            System.out.println(messenger + ":  " + message);
    }
}
