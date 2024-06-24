package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.CornerListener;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.gui.scenes.areas.HandPanel;
import it.polimi.ingsw.view.model.cards.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;

public class PlayerHandPanel extends HandPanel implements CardListener, CornerListener {
    private final GameInputHandler inputHandler;
    private ViewPlaceableCard selectedCard;

    public PlayerHandPanel(GameInputHandler inputHandler){
        super();
        this.inputHandler = inputHandler;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        switch (evt.getPropertyName()){
            case SET_STARTING_CARD, ADD_CARD_HAND:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                ViewPlaceableCard addedCard = (ViewPlaceableCard) evt.getNewValue();
                if(addedCard == null) return;
                boolean alreadyContained = cardsInHand.stream()
                        .anyMatch(e -> e.getCardID().equals(addedCard.getCardID()));
                if(alreadyContained) return;
                cardsInHand.add(addedCard);
                addedCard.setCardListener(this);
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
                if(selectedCard != null && selectedCard.equals(removedCard)) selectedCard = null; //deselect if removing selected card

                ViewPlaceableCard actualRemovedCard = cardsInHand.stream().
                        filter( c -> c.equals(removedCard)).findFirst().orElse(removedCard);

                SwingUtilities.invokeLater(
                        ()->{
                            playCardsPanel.remove(actualRemovedCard);
                            revalidate();
                            repaint();
                        }
                );
                cardsInHand.remove(actualRemovedCard);
                break;
            case CLEAR_PLAY_CARDS:
                assert evt.getOldValue() instanceof List;
                List<ViewPlayCard> removedCardsList = (List<ViewPlayCard>) evt.getOldValue();

                for(ViewPlayCard removedPlayCard : removedCardsList){
                    if(selectedCard != null && selectedCard.equals(removedPlayCard)) selectedCard = null; //deselect if removing selected card
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


    @Override
    public void placeOnCorner(String cardID, GamePoint position, CornerDirection direction)
            throws IllegalStateException {
        if(selectedCard == null){
            throw new IllegalStateException("NO CARD SELECTED.");
        }
        if(cardID == null) {
            try {
                inputHandler.placeStartCard(selectedCard.isFaceUp());
                selectedCard = null;
            } catch (IllegalStateException e){
                inputHandler.showError(e.getMessage());
                throw e; //notifies the placeholder that placement failed
            }
            return;
        }

        try {
            inputHandler.placeCard(selectedCard.getCardID(),position,
                    direction.toString(), selectedCard.isFaceUp());
            selectedCard = null; //deselect after place attempt removing selected card
        } catch (IllegalStateException e){
            inputHandler.showError(e.getMessage());
            throw e; //notifies the caller that placement failed
        }
    }

    @Override
    public void setSelectedCard(ViewPlaceableCard card) {
        //allow "deselection" of the selected card
        if(selectedCard == card){
            deselectCards();
            selectedCard = null;
            return;
        }
        selectedCard = card;
        SwingUtilities.invokeLater(
                () -> {
                    deselectCards();
                    selectedCard.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                    repaint();
                }
        );
    }

    private void deselectCards() {
        cardsInHand.forEach(
                c -> c.setBorder(BorderFactory.createEmptyBorder())
        );
    }
}
