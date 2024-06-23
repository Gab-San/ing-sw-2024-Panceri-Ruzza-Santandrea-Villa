package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GameResource;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

/**
 * A card's corner. Should always be associated with a card,
 * except during card construction.
 */
public class ViewCorner extends JComponent implements MouseListener {
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
    private boolean isDisabled;

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
        isDisabled = false;
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
        resetCorner();
    }

    public synchronized ViewPlaceableCard getCardRef() {
        return cardRef;
    }
    public synchronized void setCardRef(ViewPlaceableCard card) {
        cardRef = card;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj instanceof ViewCorner otherCorner){
            return Objects.equals(this.cardRef, otherCorner.cardRef)
                    && this.direction == otherCorner.direction;
        }
        return false;
    }
    @Override
    public int hashCode(){
        return Objects.hash(cardRef, direction);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(50,60);
    }

    @NotNull
    @Override
    public Rectangle getBounds() {
        return new Rectangle(0,0, 50, 60);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 60);
    }

    public static int getFixedWidth(){
        return 50;
    }

    public static int getFixedHeight(){
        return 60;
    }

    public void activateCorner() {
        addMouseListener(this);
        setEnabled(true);
        setVisible(true);
        Border innerBorder = BorderFactory.createLineBorder(Color.yellow, 2);
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        SwingUtilities.invokeLater(
                () -> {
                    setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
                    revalidate();
                    repaint();
                }
        );
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!isVisible || isDisabled){
            return;
        }
        cardRef.cornerListener.placeOnCorner(cardRef.getCardID(), cardRef.getPosition(), direction);
    }

    @Override
    public void mousePressed(MouseEvent e) {/*unused*/}

    @Override
    public void mouseReleased(MouseEvent e) {/*unused*/}

    @Override
    public void mouseEntered(MouseEvent e) {

        if(!isVisible || isDisabled){
            return;
        }

        setFocusable(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {

        if(!isVisible || isDisabled){
            return;
        }
        setFocusable(false);
    }

    public void disableCorner() {
        isDisabled = true;
    }

    @Override
    public String toString() {
        return "{\n" + "DIRECTION " + direction + "\n" +
                "REF " + cardRef.getCardID() + "\n" +
                "IS VISIBLE " + isVisible + "\n}";
    }

    public void resetCorner() {
        removeMouseListener(this);
        setEnabled(false);
        setVisible(false);
        setBorder(BorderFactory.createEmptyBorder());
//        SwingUtilities.invokeLater(
//                () -> {
//                    revalidate();
//                    repaint();
//                }
//        );
    }
}
