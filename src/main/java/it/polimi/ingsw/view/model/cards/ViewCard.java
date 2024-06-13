package it.polimi.ingsw.view.model.cards;

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
    private BufferedImage imageFront;
    private BufferedImage imageBack;
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
        //TODO: Fix this. MUST ADD ALL IMAGES
//        ClassLoader cl = this.getClass().getClassLoader();
//        InputStream url = null;
//        try {
//            url = cl.getResourceAsStream(imageFrontName);
//        } catch(NullPointerException e){
//            e.printStackTrace(System.err);
//        }
//
//        BufferedImage img = null;
//        try{
//            assert url != null;
//            img = ImageIO.read(url);
//        } catch(IOException e){
//            e.printStackTrace(System.err);
//        }
//        this.imageFront = img;
//
//
//        try {
//            url =  cl.getResourceAsStream(imageBackName);
//        } catch(NullPointerException e){
//            e.printStackTrace(System.err);
//        }
//
//        img = null;
//        try{
//            assert url != null;
//            img = ImageIO.read(url);
//        } catch(IOException e){
//            e.printStackTrace(System.err);
//        }
//        this.imageBack = img;
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
        // eventual GUI code here
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

    public String getImageFrontName() {
        return imageFrontName;
    }
    public String getImageBackName() {
        return imageBackName;
    }


    //DOCS: add docs for GUI paint
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        System.out.println("Painting card: " + cardID +
                "\n{IMAGE FRONT " + imageFrontName + "\n" +
                "IMAGE BACK " + imageBackName + "\n" +
                "IsFaceUp " + isFaceUp + "}");

//        g2D.drawImage(img,0,0, (int) img.getWidth(), (int) img.getHeight(), null);
        g2D.drawString("HERE SHOULD BE CARD " + cardID, 0,  0);
        g2D.setColor(Color.red);
        System.out.println("Image should be drawn");
    }

}
