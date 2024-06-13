package it.polimi.ingsw.view.model.cards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Base class of a generic card in the ViewModel
 */
public abstract class ViewCard extends JComponent {
    private final String cardID;
    private final String imageFrontName;
    private final String imageBackName;
//region GUI ATTRIBUTES
    private final BufferedImage imageFront;
    private final BufferedImage imageBack;
    public static final int IMAGE_WIDTH = 831;
    public static final int IMAGE_HEIGHT = 556;
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
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = null;
        try {
            url = cl.getResourceAsStream(imageFrontName);
        } catch(NullPointerException e){
            e.printStackTrace(System.err);
        }

        BufferedImage img = null;
        try{
            assert url != null;
            img = ImageIO.read(url);
        } catch(IOException e){
            e.printStackTrace(System.err);
        }
        this.imageFront = img;


        try {
            url =  cl.getResourceAsStream(imageBackName);
        } catch(NullPointerException e){
            e.printStackTrace(System.err);
        }

        img = null;
        try{
            assert url != null;
            img = ImageIO.read(url);
        } catch(IOException e){
            e.printStackTrace(System.err);
        }
        this.imageBack = img;
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
     * @param other the card to compare this with.
     * @return if the other object is a ViewCard and
     *        the card IDs match.
     */
    @Override
    public boolean equals(Object other){
        if(other == this) return true;
        else if(other instanceof ViewCard otherCard){
            return this.cardID.equals(otherCard.cardID);
        }else return false;
    }
    @Override
    public int hashCode(){
        return cardID.hashCode();
    }

    /**
     * @return this card's ID
     */
    public String getCardID(){
        return cardID;
    }

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
//TODO        if(!importedIMG) importIMG();
        Graphics2D g2D = (Graphics2D) g;
        BufferedImage img = isFaceUp ? imageFront : imageBack;
        // width = 831 height  = 556
        g2D.drawImage(img,0,0, img.getWidth()/4, img.getHeight()/4, null);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(IMAGE_WIDTH/4, IMAGE_HEIGHT/4);
    }

    @Override
    public int getWidth() {
        return IMAGE_WIDTH/4;
    }

    @Override
    public int getHeight() {
        return IMAGE_HEIGHT/4;
    }
    @Override
    public Dimension getSize(){
        return new Dimension(getScaledWidth(), getScaledHeight());
    }

    public static int getScaledWidth(){
        //TODO add scale factor
        return IMAGE_WIDTH/4;
    }
    public static int getScaledHeight(){
        return IMAGE_HEIGHT/4;
    }
}
