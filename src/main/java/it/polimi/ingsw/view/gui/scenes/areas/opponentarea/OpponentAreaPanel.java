package it.polimi.ingsw.view.gui.scenes.areas.opponentarea;

import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.scenes.areas.AreaPanel;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.Objects;

public class OpponentAreaPanel extends AreaPanel {
    public OpponentAreaPanel(){
        super();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("OPPONENT AREA TRIGGERED " + evt.getPropertyName());
        if(!super.parentPropertyChange(evt)) return; //returns if parent detected invalid update
        assert evt.getNewValue() instanceof ViewPlaceableCard;
        ViewPlaceableCard placedCard = (ViewPlaceableCard) evt.getNewValue();

        placedCard.forceDisableComponent();

        switch (evt.getPropertyName()){
            case ChangeNotifications.CARD_LAYER_CHANGE:
                System.out.println("OPPONENT Changed card " + placedCard.getCardID() + " layer to " + placedCard.getLayer());
                break;
            case ChangeNotifications.PLACED_CARD:
                System.out.println("OPPONENT AREA PLACING " + placedCard.getCardID() + " z=" + placedCard.getLayer());
                break;
        }
        System.out.flush();
    }
}