package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.CornerListener;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;

public class PlayerHandPanel extends JPanel implements PropertyChangeListener, CardListener, CornerListener {
    private static final int CARD_HEIGHT = ViewCard.getScaledHeight();
    private static final int CARD_WIDTH = ViewCard.getScaledWidth();
    private final List<ViewPlaceableCard> cardsInHand;
    private final GameInputHandler inputHandler;
    private final JPanel playCardsPanel;
    private final JPanel objectiveCardsPanel;
    private ViewPlaceableCard selectedCard;

    public PlayerHandPanel(GameInputHandler inputHandler){
        cardsInHand = new LinkedList<>();
        this.inputHandler = inputHandler;

        setLayout(new BorderLayout());
        setOpaque(false);
        playCardsPanel = setupPlayCardsPanel();
        objectiveCardsPanel = setupObjectiveCardsPanel();
        add(playCardsPanel, BorderLayout.CENTER);
        add(objectiveCardsPanel, BorderLayout.EAST);
    }

    private JPanel setupObjectiveCardsPanel() {
        JPanel panel = new JPanel();
        int width = CARD_WIDTH * 2 + 10;
        panel.setPreferredSize(new Dimension(width, CARD_HEIGHT));
        panel.setLayout(new GridLayout(1,2, 10, 20));
        panel.setOpaque(false);
        return panel;
    }

    private JPanel setupPlayCardsPanel() {
        JPanel panel = new JPanel();
        int width = CARD_WIDTH * 3 + 10*2;
        panel.setPreferredSize(new Dimension(width, CARD_HEIGHT));
        panel.setLayout(new GridLayout(1,3, 10, 20));
        panel.setOpaque(false);
        return panel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case SET_STARTING_CARD, ADD_CARD_HAND:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                ViewPlaceableCard addedCard = (ViewPlaceableCard) evt.getNewValue();
                if(addedCard == null) return;
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
            case ADD_SECRET_CARD:
                assert evt.getNewValue() instanceof ViewObjectiveCard;
                ViewObjectiveCard objectiveCard = (ViewObjectiveCard) evt.getNewValue();
                SwingUtilities.invokeLater(
                        () ->{
                            objectiveCardsPanel.add(objectiveCard);
                            revalidate();
                            repaint();
                        }
                );
                break;
            case CLEAR_OBJECTIVES:
                assert evt.getOldValue() instanceof List;
                List<ViewObjectiveCard> objectiveCardList = (List<ViewObjectiveCard>) evt.getOldValue();
                for(ViewObjectiveCard objCard : objectiveCardList){
                    SwingUtilities.invokeLater(
                            () ->{
                                objectiveCardsPanel.remove(objCard);
                                revalidate();
                                repaint();
                            }
                    );
                }
                break;
        }
    }


    @Override
    public void setClickedCard(String cardID, GamePoint position, CornerDirection direction)
            throws IllegalStateException {
        if(selectedCard == null){
            throw new IllegalStateException("NO CARD SELECTED.");
        }
        if(cardID == null) {
            try {
                inputHandler.placeStartCard(selectedCard.isFaceUp());
                selectedCard = null;
            } catch (RemoteException e) {
                inputHandler.showError("CONNECTION LOST."); //TODO: review this notification
                inputHandler.notifyDisconnection();
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
        } catch (RemoteException e) {
            inputHandler.showError("CONNECTION LOST."); //TODO: review this notification
            inputHandler.notifyDisconnection();
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
                }
        );
    }

    private void deselectCards() {
        cardsInHand.forEach(
                c -> c.setBorder(BorderFactory.createEmptyBorder())
        );
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CARD_WIDTH * 5 + 10 * 4, CARD_HEIGHT);
    }
    @Override
    public int getHeight() {
        return (int) getPreferredSize().getHeight();
    }
    @Override
    public int getWidth() {
        return (int) getPreferredSize().getWidth();
    }

}
