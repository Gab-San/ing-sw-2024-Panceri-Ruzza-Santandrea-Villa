package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.view.gui.GUIFunc;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class acts as a placeholder for a placeable card.
 * <p>
 *     Since the the first cards to be placed on a play area need
 *     a clickable space to be defined.
 *     The place holder offers a clickable space on which to place
 *     the first card.
 * </p>
 */
public class PlaceHolder extends JComponent {
    private static final String PATH = GUIFunc.getGraphicsResourcesRootPath() + "icons/TransparentStarting.png";
    private final BufferedImage image;

    /**
     * Default constructor.
     */
    public PlaceHolder(){
        super();
        this.image = GUIFunc.importIMG(this.getClass().getClassLoader(), PATH);
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
