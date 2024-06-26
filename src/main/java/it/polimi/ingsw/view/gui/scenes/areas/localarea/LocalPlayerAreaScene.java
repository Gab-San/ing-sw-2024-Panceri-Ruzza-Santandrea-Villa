package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.scenes.areas.AreaScene;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import java.beans.PropertyChangeEvent;

/**
 * This class represents the board view of the local user. It offers interactive
 * views of the play-area and player's hand.
 */
public class LocalPlayerAreaScene extends AreaScene{
    private final GameInputHandler inputHandler;

    /**
     * Constructs board view for the local user handled by the given
     * input handler.
     * @param inputHandler game input handler
     */
    public LocalPlayerAreaScene(GameInputHandler inputHandler){
        super(new PlayAreaPanel(), new PlayerHandPanel(inputHandler));
        this.inputHandler = inputHandler;
        PlayAreaPanel playAreaPanel = (PlayAreaPanel) areaPanel;
        playAreaPanel.setCornerListener((PlayerHandPanel) super.handPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        boolean isLocal;
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                assert evt.getNewValue() instanceof ViewHand;
                ViewHand hand = (ViewHand) evt.getNewValue();
                isLocal = inputHandler.isLocalPlayer(hand.getNickname());
                if(!isLocal){
                    return;
                }
                break;
            case ChangeNotifications.ADDED_AREA:
                assert evt.getNewValue() instanceof ViewPlayArea;
                ViewPlayArea playArea = (ViewPlayArea) evt.getNewValue();
                isLocal = inputHandler.isLocalPlayer(playArea.getOwner());
                if(!isLocal){
                    return;
                }
                break;
        }
        super.propertyChange(evt);
    }
}
