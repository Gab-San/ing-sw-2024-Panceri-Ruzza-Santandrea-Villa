package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.gui.CornerListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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

    protected CornerListener cornerListener;

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
        this.removeMouseListener(this);
        this.setFocusable(false);
        // Disabling input on this component
        this.getInputMap().remove(KeyStroke.getKeyStroke("F"));
    }

    public void activateCorner(CornerDirection direction){
        corners.get(direction).activateCorner();
        SwingUtilities.invokeLater(
                () -> {
                    revalidate();
                    repaint();
                }
        );
    }
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
    }

    public synchronized void disableComponent(){
        this.setEnabled(false);
        for(ViewCorner corner : corners.values()){
            corner.setEnabled(false);
            corner.setVisible(false);
            corner.disableCorner();
            corner.resetCorner();
            remove(corner);
        }
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
        card.setEnabled(true);
        card.grabFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        card.setFocusable(false);
        card.setEnabled(false);
    }
}

class FlipAction extends AbstractAction{

    @Override
    public void actionPerformed(ActionEvent e) {
        assert e.getSource() instanceof ViewPlaceableCard;
        ViewPlaceableCard card = (ViewPlaceableCard) e.getSource();
        card.flip();
    }
}