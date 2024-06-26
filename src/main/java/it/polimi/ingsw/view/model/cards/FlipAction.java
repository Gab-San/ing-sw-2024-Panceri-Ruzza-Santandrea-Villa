package it.polimi.ingsw.view.model.cards;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This class implements the action that flips cards.
 */
public class FlipAction extends AbstractAction {
    /**
     * Default constructor.
     */
    public FlipAction(){}
    @Override
    public void actionPerformed(ActionEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        if(!card.isEnabled()) {
            return;
        }
        card.flip();
    }
}