package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;

import javax.swing.*;

/**
 * A card's corner. Should always be associated with a card,
 * except during card construction.
 */
public class ViewCorner extends JComponent {
    private final GameResource frontResource;
    private final GameResource backResource;
    /**
     * The card this corner belongs to
     */
    private ViewPlaceableCard cardRef;
    /**
     * Direction indicates which corner of cardRef this corner represents.
     */
    private final CornerDirection direction;
    /**
     * True if this corner is not covered by another corner
     */
    private boolean isVisible;

    /**
     * Constructs the corner, optionally already attaching it to a card
     * @param frontResource resource on the front face of the corner
     * @param backResource resource on the back face of the corner
     * @param direction indicates which corner of cardRef this corner represents
     * @param cardRef the card this corner belongs to (or null if the card will be constructed with this corner)
     */
    public ViewCorner(GameResource frontResource, GameResource backResource, CornerDirection direction, ViewPlaceableCard cardRef){
        this.frontResource = frontResource;
        this.backResource = backResource;
        this.cardRef = cardRef;
        this.direction = direction;
        isVisible = true;
    }

    /**
     * Constructs the corner, without attaching it to any card
     * @param frontResource resource on the front face of the corner
     * @param backResource resource on the back face of the corner
     * @param direction indicates which corner of cardRef this corner represents
     */
    public ViewCorner(GameResource frontResource, GameResource backResource, CornerDirection direction){
        this(frontResource, backResource, direction, null);
    }

    /**
     * Constructs this corner as a copy of another corner
     * @param other the corner to copy.
     */
    public ViewCorner(ViewCorner other){
        this(other.frontResource, other.backResource, other.direction, other.cardRef);
    }

    /**
     * @return The resource on the face corresponding to the attached card's status
     * @throws NullPointerException if the corner is not attached to any card (cardRef==null)
     */
    public GameResource getResource() throws NullPointerException {
        return cardRef.isFaceUp() ? frontResource : backResource;
    }
    public CornerDirection getDirection() {
        return direction;
    }

    public synchronized boolean isVisible(){
        return isVisible;
    }

    /**
     * Permanently sets this corner's "visible" status to false.
     */
    public synchronized void cover(){
        isVisible = false;
    }

    public synchronized ViewPlaceableCard getCardRef() {
        return cardRef;
    }
    public synchronized void setCardRef(ViewPlaceableCard card) {
        cardRef = card;
    }
}
