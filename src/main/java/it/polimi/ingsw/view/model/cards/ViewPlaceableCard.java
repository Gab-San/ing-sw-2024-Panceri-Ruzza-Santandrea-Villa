package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.gui.CornerListener;

import javax.swing.*;
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
     * Listener for card corners.
     */
    protected CornerListener cornerListener;
    private int layer;

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
     * Returns this card's colour as a resource. <br>
     * (e.g. purple {@code <->} Butterfly resource)
     * @return this card's colour as a resource.
     */
    abstract public GameResource getCardColour();

    /**
     * Returns the resource on the visible side of the corner in the given direction.
     * @param dir the corner's direction on this card
     * @return the resource on the visible side of the corner in the given direction.
     */
    public GameResource getCornerResource(CornerDirection dir){
        return corners.get(dir).getResource();
    }

    /**
     * Returns the corner in the given direction
     * @param dir the corner's direction on this card
     * @return this card's corner in the given direction
     */
    public ViewCorner getCorner(CornerDirection dir) {
        return corners.get(dir);
    }

    /**
     * Sets card position and activates card for positioning.
     * @param position position in which to place the card
     */
    public synchronized void setPosition(GamePoint position) {
        this.position = position;
        super.disableComponent();
        if(Arrays.stream(this.getMouseListeners()).anyMatch(e -> e == this))
            this.removeMouseListener(this);
        this.setFocusable(false);
        // Disabling input on this component
        this.getInputMap().remove(KeyStroke.getKeyStroke("F"));
    }

    /**
     * Returns card position (might be null if the card was not positioned yet).
     * @return card position
     */
    public synchronized GamePoint getPosition(){
        return position;
    }

    /**
     * This method sets the card listener to listen to this card.
     * @param cornerListener listener to this card actions
     */
    public synchronized void setCornerListener(CornerListener cornerListener){
        this.cornerListener = cornerListener;
    }
    private void setupViewCard(){
        setLayout(new GridLayout(2,2,115,30));
        for(ViewCorner corner: corners.values()){
            corner.setEnabled(false);
            corner.setFocusable(false);
        }
        this.setFocusable(false);
        this.addMouseListener(this);
        corners.keySet().stream()
                .sorted(CornerDirection.getComparator())
                .forEach((dir) -> add(corners.get(dir)));
        // Enabling flipping input
        this.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("F"), "FLIP_CARD_ACTION");
        this.getActionMap().put("FLIP_CARD_ACTION", new FlipAction());

        layer = 0;
    }

    /**
     * Disables component.
     */
    public synchronized void forceDisableComponent(){
        disableComponent();
        for(ViewCorner corner : corners.values()){
            corner.setEnabled(false);
            corner.setVisible(false);
            corner.disableCorner();
            corner.resetCorner();
            remove(corner);
        }
        SwingUtilities.invokeLater(
                ()->{
                    setVisible(true);
                    revalidate();
                    repaint();
                }
        );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard selectedCard = (ViewPlaceableCard) e.getSource();
        cardListener.setSelectedCard(selectedCard);
    }

    @Override
    public void mousePressed(MouseEvent e) {/*unused*/}

    @Override
    public void mouseReleased(MouseEvent e) {/*unused*/}

    @Override
    public void mouseEntered(MouseEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        card.setFocusable(true);

        card.requestFocusInWindow();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        card.setFocusable(false);
    }
    /**
     * Setter for this card's z layer. <br>
     * The layer is used to display overlapping cards in GUI.
     * @param layer this card's z layer.
     */
    public void setLayer(int layer){
        this.layer = layer;
    }
    /**
     * Getter for this card's z layer. <br>
     * The layer is used to display overlapping cards in GUI.
     * @return this card's z layer.
     */
    public int getLayer() {
        return layer;
    }
}