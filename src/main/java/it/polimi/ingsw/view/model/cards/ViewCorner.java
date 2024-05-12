package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.model.cards.PlaceableCard;
import it.polimi.ingsw.model.enums.CornerDirection;
import it.polimi.ingsw.model.enums.GameResource;

public class ViewCorner {
    private final GameResource frontResource;
    private final GameResource backResource;
    private boolean visible;
    private ViewPlaceableCard cardRef;
    private final CornerDirection direction;

    public ViewCorner(GameResource frontResource, GameResource backResource, CornerDirection direction, ViewPlaceableCard cardRef){
        this.frontResource = frontResource;
        this.backResource = backResource;
        this.direction = direction;
        visible = true;
        this.cardRef = cardRef;
    }
    public ViewCorner(GameResource frontResource, GameResource backResource, CornerDirection direction){
        this(frontResource, backResource, direction, null);
    }

    public GameResource getFrontResource() {
        return frontResource;
    }
    public GameResource getBackResource() {
        return backResource;
    }

    public boolean isVisible() {
        return visible;
    }
    public void cover() {
        this.visible = false;
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
