package it.polimi.ingsw.view.gui.scenes.areas.opponentarea;

import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.scenes.areas.AreaScene;
import it.polimi.ingsw.view.model.*;

import java.beans.PropertyChangeEvent;

public class OpponentAreaScene extends AreaScene {
    private final String nickname;
    private final OpponentAreaPanel opponentAreaPanel;
    private final OpponentHandPanel opponentHandPanel;

    public OpponentAreaScene(GameInputHandler inputHandler, String nickname) {
        super(inputHandler, new OpponentAreaPanel(), new OpponentHandPanel());
        opponentHandPanel = (OpponentHandPanel) handPanel;
        opponentAreaPanel = (OpponentAreaPanel) areaPanel;
        this.nickname = nickname;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                assert evt.getNewValue() instanceof ViewHand;
                ViewHand hand = (ViewHand) evt.getNewValue();
                if(!nickname.equals(hand.getNickname())){
                    System.out.println("QUITTING OPPONENT HAND " + hand.getNickname());
                    return;
                }
                break;
            case ChangeNotifications.ADDED_AREA:
                assert evt.getNewValue() instanceof ViewPlayArea;
                ViewPlayArea playArea = (ViewPlayArea) evt.getNewValue();
                if(!nickname.equals(playArea.getOwner())){
                    System.out.println("QUITTING ADDED_AREA " + playArea.getOwner());
                    return;
                }
                break;
        }
        super.propertyChange(evt);
    }
}
