package it.polimi.ingsw.network.rmi.update;

import it.polimi.ingsw.view.ModelUpdater;

public class DisplayMessageUpdate extends RMIUpdate {

    private final String messenger, message;

    public DisplayMessageUpdate(ModelUpdater modelUpdater, String messenger, String message) {
        super(modelUpdater);
        this.messenger = messenger;
        this.message = message;
    }

    @Override
    public void update() {
        if(modelUpdater != null)
            modelUpdater.displayMessage(messenger, message);
        else
            System.out.println(messenger + ":  " + message);
    }
}
