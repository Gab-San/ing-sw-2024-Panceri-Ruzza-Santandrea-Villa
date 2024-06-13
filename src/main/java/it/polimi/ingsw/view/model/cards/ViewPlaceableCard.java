package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.GamePoint;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.List;

/**
 * The base class of PlaceableCards in the ViewModel
 */
public abstract class ViewPlaceableCard extends ViewCard implements MouseListener{
    /**
     * Map associating a direction with this card's corner in that direction
     */
    private final Map<CornerDirection, ViewCorner> corners;
    /**
     * This card's position after being placed on a playArea
     */
    private GamePoint position;

    /**
     * Constructs the placeable card base.
     * @param cardID this card's ID
     * @param imageFrontName the front image file name
     * @param imageBackName the back image file name
     * @param corners list of corners (not attached to another card)
     */
    public ViewPlaceableCard(String cardID, String imageFrontName, String imageBackName, List<ViewCorner> corners) {
        super(cardID, imageFrontName, imageBackName);

        Map<CornerDirection, ViewCorner> thisCorners = new Hashtable<>();
        for(ViewCorner c : corners) {
            ViewCorner corner = new ViewCorner(c);
            corner.setCardRef(this);
            if(thisCorners.put(corner.getDirection(), corner) != null)
                throw new IllegalArgumentException("Corners with duplicate directions were passed in ViewPlaceableCard constructor");
        }

        this.corners = Collections.unmodifiableMap(thisCorners);
        this.position = null;
        setupViewCard();
    }
    /**
     * Construct the placeable card as a copy of another placeable card.
     * @param other the other placeable card to copy.
     */
    public ViewPlaceableCard(ViewPlaceableCard other){
        super(other);
        Map<CornerDirection, ViewCorner> thisCorners = new Hashtable<>();
        other.corners.values().forEach(
                c -> thisCorners.put(c.getDirection(), new ViewCorner(c))
        );
        thisCorners.values().forEach(c -> c.setCardRef(this));
        this.corners = Collections.unmodifiableMap(thisCorners);
        setupViewCard();
    }

    /**
     * @return this card's colour as a resource. (e.g. purple <-> Butterfly resource)
     */
    abstract public GameResource getCardColour();

    /**
     * @param dir the corner's direction on this card
     * @return the resource on that corner on the current visible side of this card
     */
    public GameResource getCornerResource(CornerDirection dir){
        return corners.get(dir).getResource();
    }

    /**
     * @param dir the corner's direction on this card
     * @return this card's corner in the given direction
     */
    public ViewCorner getCorner(CornerDirection dir) {
        return corners.get(dir);
    }

    public synchronized void setPosition(GamePoint position) {
        this.position = position;
        enableCorners();
        removeMouseListener(this);
    }

    private void enableCorners(){
        for(ViewCorner corn: corners.values()){
            corn.setEnabled(true);
            corn.addMouseListener(this);
        }
    }

    public synchronized GamePoint getPosition(){
        return position;
    }

    private void setupViewCard(){
        setLayout(new GridLayout(2,2,100,100));
        for(ViewCorner corner: corners.values()){
            corner.setEnabled(false);
            corner.setFocusable(false);
        }
        corners.keySet().stream()
                .sorted(CornerDirection.getComparator())
                .forEach((dir) -> add(corners.get(dir)));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("SELECTED: " + ((ViewCorner) e.getSource()).getDirection());
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
