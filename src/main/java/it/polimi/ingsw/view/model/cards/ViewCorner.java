package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.view.model.enums.GameResourceView;

public class ViewCorner {
    private final GameResourceView frontResource;
    private final GameResourceView backResource;
    private ViewPlaceableCard cardRef;
    private final CornerDirection direction;
    private boolean isVisible;

    public ViewCorner(GameResourceView frontResource, GameResourceView backResource, CornerDirection direction, ViewPlaceableCard cardRef){
        this.frontResource = frontResource;
        this.backResource = backResource;
        this.cardRef = cardRef;
        this.direction = direction;
        isVisible = true;
    }
    public ViewCorner(GameResourceView frontResource, GameResourceView backResource, CornerDirection direction){
        this(frontResource, backResource, direction, null);
    }
    public ViewCorner(ViewCorner other){
        this(other.frontResource, other.backResource, other.direction, other.cardRef);
    }

    public GameResourceView getResource() {
        return cardRef.isFaceUp ? frontResource : backResource;
    }
    public boolean isVisible(){
        return isVisible;
    }
    public void cover(){
        isVisible = false;
    }
    public ViewPlaceableCard getCardRef() {
        return cardRef;
    }
    public void setCardRef(ViewPlaceableCard card) {
        cardRef = card;
    }
    public CornerDirection getDirection() {
        return direction;
    }
}
