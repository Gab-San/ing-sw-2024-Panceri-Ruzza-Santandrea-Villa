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
        super.propertyChange(evt);
//        System.out.println("OPPONENT AREA "+ this.hashCode() +" TRIGGERED " + evt.getPropertyName());
        ViewPlaceableCard placedCard;
        switch (evt.getPropertyName()){
            case ChangeNotifications.CARD_LAYER_CHANGE:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                placedCard = (ViewPlaceableCard) evt.getNewValue();
                if (!placedCardsSet.contains(placedCard)) return;

                System.out.println("OPPONENT Changed card " + placedCard.getCardID() + " layer to " + placedCard.getLayer());
                System.out.flush();
                break;
            case ChangeNotifications.PLACED_CARD:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                placedCard = (ViewPlaceableCard) evt.getNewValue();
                if (placedCardsSet.contains(placedCard)) return;
                System.out.println("OPPONENT AREA PLACING " + placedCard.getCardID() + " z=" + placedCard.getLayer());
                placedCard.forceDisableComponent();
                break;
        }
    }
}