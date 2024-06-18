package it.polimi.ingsw.view.gui.scenes.areas.opponentarea;

import it.polimi.ingsw.view.gui.scenes.areas.HandPanel;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.List;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;

public class OpponentHandPanel extends HandPanel {
    public OpponentHandPanel(){
        super();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        System.out.println("OPPONENT HAND TRIGGERED " + evt.getPropertyName());
        switch (evt.getPropertyName()){
            case SET_STARTING_CARD, ADD_CARD_HAND:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                ViewPlaceableCard addedCard = (ViewPlaceableCard) evt.getNewValue();
                if(addedCard == null) return;
                boolean alreadyContained = cardsInHand.stream()
                        .anyMatch(e -> e.getCardID().equals(addedCard.getCardID()));
                if(alreadyContained) return;
                cardsInHand.add(addedCard);

                addedCard.forceDisableComponent();
                SwingUtilities.invokeLater(
                        () -> {
                            playCardsPanel.add(addedCard);
                            revalidate();
                            repaint();
                        }
                );
                break;
            case CLEAR_STARTING_CARD, REMOVE_CARD_HAND:
                assert evt.getOldValue() instanceof ViewPlaceableCard;
                ViewPlaceableCard removedCard = (ViewPlaceableCard) evt.getOldValue();
                if(removedCard == null || !cardsInHand.contains(removedCard)) return;

                SwingUtilities.invokeLater(
                        ()->{
                            playCardsPanel.remove(removedCard);
                            revalidate();
                            repaint();
                        }
                );
                cardsInHand.remove(removedCard);
                break;
            case CLEAR_PLAY_CARDS:
                assert evt.getOldValue() instanceof List;
                List<ViewPlayCard> removedCardsList = (List<ViewPlayCard>) evt.getOldValue();

                for(ViewPlayCard removedPlayCard : removedCardsList){
                    SwingUtilities.invokeLater(
                            ()->{
                                playCardsPanel.remove(removedPlayCard);
                                revalidate();
                                repaint();
                            }
                    );
                    cardsInHand.remove(removedPlayCard);
                }
                break;
        }
    }
}
