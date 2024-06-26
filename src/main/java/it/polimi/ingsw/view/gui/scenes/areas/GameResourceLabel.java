package it.polimi.ingsw.view.gui.scenes.areas;

import javax.swing.*;

/**
 * This class represents a game visible resource to display.
 * Each visible resource label is characterized by the game resource it refers to
 * and the count of current visible resources of that type.
 */
public class GameResourceLabel extends JLabel {
    /**
     * Constructs the game resource label of the desired resource.
     * The resource is defined by the chosen icon (one for each resource).
     * @param resourceIcon image of the desired resource
     * @param initialQuantity initial quantity of that resource
     */
    public GameResourceLabel(ImageIcon resourceIcon, int initialQuantity){
        super(resourceIcon);
        setText("x" + initialQuantity);
    }

    /**
     * Updates the displaying quantity of the desired resource.
     * @param quantity updated visible quantity
     */
    public void setQuantity(int quantity){
        setText("x" + quantity);
        revalidate();
        repaint();
    }
}
