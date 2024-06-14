package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;

public class PlayerHandPanel extends JPanel implements PropertyChangeListener, CardListener{
    private static final int HEIGHT = ViewCard.getScaledHeight();
    private static final int WIDTH = ViewCard.getScaledWidth() * 4 + 10 * 3;
    private final List<ViewPlaceableCard> cardsInHand;
    private final GameInputHandler inputHandler;
    private ViewObjectiveCard secretObjective;
    private static final int MAX_PLAY_CARDS = 3;

    private ViewPlaceableCard selectedCard;

    public PlayerHandPanel(GameInputHandler inputHandler){
        cardsInHand = new LinkedList<>();
        this.inputHandler = inputHandler;


        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(1,4, 10, 10));
        setOpaque(false);
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ViewPlaceableCard card;
        switch (evt.getPropertyName()){
            case SET_STARTING_CARD, ADD_CARD_HAND:
                assert evt.getNewValue() instanceof ViewPlaceableCard;
                card = (ViewPlaceableCard) evt.getNewValue();
                if(card == null) return;

                cardsInHand.add(card);
                card.setCardListener(this);
                SwingUtilities.invokeLater(
                        () -> {
                            add(card);
                            revalidate();
                            repaint();
                        }
                );
                break;
            case CLEAR_STARTING_CARD, REMOVE_CARD_HAND:
                assert evt.getOldValue() instanceof ViewPlaceableCard;
                card = (ViewPlaceableCard) evt.getOldValue();
                if(!cardsInHand.contains(card)){
                    return;
                }
                card.setCardListener(null);
                cardsInHand.remove(card);
                SwingUtilities.invokeLater(
                        ()->{
                            remove(card);
                            revalidate();
                            repaint();
                        }
                );
                break;
            case CHOSEN_OBJECTIVE_CARD:
                assert evt.getNewValue() instanceof ViewObjectiveCard;
                secretObjective = (ViewObjectiveCard) evt.getNewValue();
                SwingUtilities.invokeLater(
                        () ->{
                            add(secretObjective, MAX_PLAY_CARDS);
                            revalidate();
                            repaint();
                        }
                );
                break;
        }
    }


    @Override
    public void setClickedCard(String cardID, int x, int y, CornerDirection direction)
            throws IllegalStateException {
        if(selectedCard == null){
            throw new IllegalStateException("NO CARD SELECTED.");
        }
        if(cardID == null) {
            try {
                inputHandler.placeStartCard(selectedCard.isFaceUp());
            } catch (RemoteException e) {
                //TODO notify disconnection on screen
                inputHandler.notifyDisconnection();
            }
            return;
        }

        try {
            inputHandler.placeCard(selectedCard.getCardID(),new GamePoint(x,y),
                    direction.toString(), selectedCard.isFaceUp());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setSelectedCard(ViewPlaceableCard card) {
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

}
