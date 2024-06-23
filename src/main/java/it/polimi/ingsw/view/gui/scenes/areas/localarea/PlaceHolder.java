package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PlaceHolder extends JComponent {

    private BufferedImage image;

    public PlaceHolder(){
        super();
        importIMG();
    }

    private void importIMG() {
        ClassLoader cl = this.getClass().getClassLoader();
        InputStream url = null;
        try {
            url = cl.getResourceAsStream("icons/TransparentStarting.png");
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
        this.image = img;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        // width = 831 height  = 556
        g2D.drawImage(image,0,0, image.getWidth()/4, image.getHeight()/4, null);
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return new Dimension(getWidth(),getHeight());
    }

    @Override
    public int getWidth() {
        return image.getWidth()/4;
    }

    @Override
    public int getHeight() {
        return image.getHeight()/4;
    }
}
