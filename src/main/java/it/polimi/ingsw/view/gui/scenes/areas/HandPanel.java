package it.polimi.ingsw.view.gui.scenes.areas;

import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.cards.ViewCard;
import it.polimi.ingsw.view.model.cards.ViewObjectiveCard;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;
import it.polimi.ingsw.view.model.cards.ViewPlayCard;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.gui.ChangeNotifications.*;


public abstract class HandPanel extends JPanel implements PropertyChangeListener {
    private static final int CARD_HEIGHT = ViewCard.getScaledHeight();
    private static final int CARD_WIDTH = ViewCard.getScaledWidth();
    protected final List<ViewPlaceableCard> cardsInHand;
    protected final JPanel playCardsPanel;
    protected final JPanel objectiveCardsPanel;

    public HandPanel(){
        cardsInHand = new LinkedList<>();

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
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

}
