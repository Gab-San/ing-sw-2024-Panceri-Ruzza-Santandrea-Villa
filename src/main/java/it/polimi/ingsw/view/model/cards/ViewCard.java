package it.polimi.ingsw.view.model.cards;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public abstract class ViewCard extends JComponent {
    private final String cardID;
    private final String imageFrontName;
    private final String imageBackName;
    private BufferedImage imageFront;
    private BufferedImage imageBack;
    protected boolean isFaceUp;

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

    public ViewCard(ViewCard other){
        this(other.cardID, other.imageFrontName, other.imageBackName);
        this.isFaceUp = other.isFaceUp;
    }

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

    public String getCardID(){
        return cardID;
    }
    public synchronized boolean isFaceUp(){
        return isFaceUp;
    }
    public synchronized void flip(){
        isFaceUp = !isFaceUp;
        // eventual GUI code here
    }
    public synchronized void turnFaceUp(){
        if(!isFaceUp) flip();
    }
    public synchronized void turnFaceDown(){
        if(isFaceUp) flip();
    }

    public String getImageFrontName() {
        return imageFrontName;
    }
    public String getImageBackName() {
        return imageBackName;
    }


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
