package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.scenes.areas.AreaScene;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;

import java.beans.PropertyChangeEvent;
//DOCS add docs
public class LocalPlayerAreaScene extends AreaScene{

    public LocalPlayerAreaScene(GameInputHandler inputHandler){
        super(inputHandler, new PlayAreaPanel(), new PlayerHandPanel(inputHandler));

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
