package it.polimi.ingsw.view.gui.scenes.areas.opponentarea;

import it.polimi.ingsw.view.gui.scenes.areas.AreaPanel;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import java.beans.PropertyChangeEvent;
//DOCS add docs
public class OpponentAreaPanel extends AreaPanel {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(!super.parentPropertyChange(evt))
            return; //returns if parent detected invalid update

        if(evt.getNewValue() instanceof ViewPlaceableCard placedCard){
            placedCard.forceDisableComponent();
        }
    }
}