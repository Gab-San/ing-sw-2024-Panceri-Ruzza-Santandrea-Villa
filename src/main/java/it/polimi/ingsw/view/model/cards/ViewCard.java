package it.polimi.ingsw.view.model.cards;

import it.polimi.ingsw.view.gui.CardListener;
import it.polimi.ingsw.view.gui.GUIFunc;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Base class of a generic card in the ViewModel
 */
public abstract class ViewCard extends JComponent {
    private final String cardID;
    private final String imageFrontName;
    private final String imageBackName;
//region GUI ATTRIBUTES
    private BufferedImage imageFront;
    private BufferedImage imageBack;
    private boolean importedImg;
    /**
     * Pixel image width.
     */
    public static final int IMAGE_WIDTH = 831;
    /**
     * Pixel image height.
     */
    public static final int IMAGE_HEIGHT = 556;
    /**
     * Scaling image factor.
     */
    public static final int SCALE_FACTOR = 4;
    /**
     * Card listener to this card.
     */
    protected CardListener cardListener;
//endregion
    /**
     * True if this card is front-face up
     */
    protected boolean isFaceUp;

    /**
     * Constructs the card base.
     * @param cardID this card's ID
     * @param imageFrontName the front image file name
     * @param imageBackName the back image file name
     */
    public ViewCard(String cardID, String imageFrontName, String imageBackName){
        this.cardID = cardID;
        this.imageFrontName = imageFrontName;
        this.imageBackName = imageBackName;
        this.isFaceUp = false;
        importedImg = false;
    }

    /**
     * Construct the card base as a copy of another card.
     * @param other the other card to copy.
     */
    public ViewCard(ViewCard other){
        this(other.cardID, other.imageFrontName, other.imageBackName);
        this.isFaceUp = other.isFaceUp;
    }

    /**
     * Implementation of the equals method.
     * @param other the card to compare this with.
     * @return true if the other object is a ViewCard and
     *        the card IDs match.
     */
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        else if(other instanceof ViewCard otherCard){
            return this.cardID.equals(otherCard.cardID);
        }else return false;
    }

    /**
     * Implementation of the hashcode method.
     * @return the hashcode of this card's ID.
     */
    @Override
    public int hashCode(){
        return cardID.hashCode();
    }

    /**
     * Getter of the card ID
     * @return this card's ID
     */
    public String getCardID(){
        return cardID;
    }

    /**
     * Getter of this card's isFaceUp parameter.
     * @return true if this card is front-face up.
     */
    public synchronized boolean isFaceUp(){
        return isFaceUp;
    }

    /**
     * Flips this card, inverting its "faceUp" status
     */
    public synchronized void flip(){
        isFaceUp = !isFaceUp;
        SwingUtilities.invokeLater(
                () ->{
                    revalidate();
                    repaint();
                }
        );
    }
    /**
     * Sets the "faceUp" status to true
     */
    public synchronized void turnFaceUp(){
        if(!isFaceUp) flip();
    }
    /**
     * Sets the "faceUp" status to false
     */
    public synchronized void turnFaceDown(){
        if(isFaceUp) flip();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(!importedImg) importIMG();
        Graphics2D g2D = (Graphics2D) g;
        BufferedImage img = isFaceUp ? imageFront : imageBack;
        // width = 831 height  = 556
        g2D.drawImage(img,0,0, img.getWidth()/4, img.getHeight()/4, null);
    }

    private void importIMG() {
        ClassLoader cl = this.getClass().getClassLoader();
        this.imageFront = GUIFunc.importIMG(cl, imageFrontName);
        this.imageBack = GUIFunc.importIMG(cl, imageBackName);
        importedImg = true;
    }

    /**
     * This method sets the card listener to listen to this card.
     * @param cardListener listener to this card actions
     */
    public void setCardListener(CardListener cardListener){
        this.cardListener = cardListener;
    }

    /**
     * Disables the component.
     */
    public void disableComponent(){
        this.setEnabled(false);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(IMAGE_WIDTH/SCALE_FACTOR, IMAGE_HEIGHT/SCALE_FACTOR);
    }

    @Override
    public int getWidth() {
        return IMAGE_WIDTH/SCALE_FACTOR;
    }

    @Override
    public int getHeight() {
        return IMAGE_HEIGHT/SCALE_FACTOR;
    }
    @Override
    public Dimension getSize(){
        return new Dimension(getScaledWidth(), getScaledHeight());
    }

    /**
     * Returns the width of the card image scaled by the scale factor.
     * @return the width of the card image scaled by the scale factor.
     */
    public static int getScaledWidth(){
        return IMAGE_WIDTH/SCALE_FACTOR;
    }
    /**
     * Returns the height of the card image scaled by the scale factor.
     * @return the height of the card image scaled by the scale factor.
     */
    public static int getScaledHeight(){
        return IMAGE_HEIGHT/SCALE_FACTOR;
    }

    @NotNull
    @Override
    public Rectangle getBounds() {
        return new Rectangle(0,0, getWidth(), getHeight());
    }
}
