package it.polimi.ingsw.view.model;

import it.polimi.ingsw.view.model.cards.ViewCard;

public record ViewDeck<C extends ViewCard> (
    boolean isEmpty,
    C firstRevealed,
    C secondRevealed
){
    public ViewDeck(){
        this(false, null, null);
    }

}
