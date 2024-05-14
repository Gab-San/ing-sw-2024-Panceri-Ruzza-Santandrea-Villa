package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.view.model.enums.GameResourceView;

public class ViewCorner {
    private final GameResourceView frontResource;
    private final GameResourceView backResource;
    private ViewPlaceableCard cardRef;
    private final CornerDirection direction;

    public ViewCorner(GameResourceView frontResource, GameResourceView backResource, CornerDirection direction, ViewPlaceableCard cardRef){
        this.frontResource = frontResource;
        this.backResource = backResource;
        this.cardRef = cardRef;
        this.direction = direction;
    }
    public ViewCorner(GameResourceView frontResource, GameResourceView backResource, CornerDirection direction){
        this(frontResource, backResource, direction, null);
    }

    public GameResourceView getFrontResource() {
        return frontResource;
    }
    public GameResourceView getBackResource() {
        return backResource;
    }

    public ViewPlaceableCard getCardRef() {
        return cardRef;
    }
    public void setCardRef(ViewPlaceableCard cardRef) {
        this.cardRef = cardRef;
    }

    public CornerDirection getDirection() {
        return direction;
    }
}
