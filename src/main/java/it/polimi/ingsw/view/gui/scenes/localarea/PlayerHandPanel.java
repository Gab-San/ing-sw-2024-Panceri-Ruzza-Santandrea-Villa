package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class PlayerHandPanel extends JPanel implements PropertyChangeListener, CardListener, MouseListener {
    private static final int HEIGHT = ViewCard.getScaledHeight();
    private static final int WIDTH = ViewCard.getScaledWidth() * 3 + 10 * 3;
    private final List<ViewPlaceableCard> cardsInHand;
    private final GameInputHandler inputHandler;
    private final List<ViewObjectiveCard> secretObjectives;

    private ViewPlaceableCard selectedCard;

    public PlayerHandPanel(GameInputHandler inputHandler){
        cardsInHand = new LinkedList<>();
        this.inputHandler = inputHandler;
        secretObjectives = new LinkedList<>();


        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setSize(WIDTH, HEIGHT);
        setLayout(new GridLayout(1,3, 10, 10));
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
                SwingUtilities.invokeLater(
                        () -> {
                            add(card);
                            card.addMouseListener(this);
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
                cardsInHand.remove(card);
                SwingUtilities.invokeLater(
                        ()->{
                            remove(card);
                            revalidate();
                            repaint();
                        }
                );
                break;
        }
    }

    private void deselectCards() {
        cardsInHand.forEach(
                c -> c.setBorder(BorderFactory.createEmptyBorder())
        );
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
                selectedCard.removeMouseListener(this);
            } catch (RemoteException e) {
                //TODO notify disconnection on screen
                inputHandler.notifyDisconnection();
            }
            return;
        }

        try {
            inputHandler.placeCard(selectedCard.getCardID(),new GamePoint(x,y),
                    direction.toString(), selectedCard.isFaceUp());
            selectedCard.removeMouseListener(this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        deselectCards();
        selectedCard = card;
        card.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
    }

    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        System.out.println("ENTERING CARD + " + card.getCardID());
    }

}
