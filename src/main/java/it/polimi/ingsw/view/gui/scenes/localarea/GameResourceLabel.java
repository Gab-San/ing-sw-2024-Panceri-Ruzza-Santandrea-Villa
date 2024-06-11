package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.GameResource;

import javax.swing.*;

public class GameResourceLabel extends JLabel {
    public GameResourceLabel(ImageIcon resourceIcon, int initialQuantity){
        super(resourceIcon);
        setText("x" + initialQuantity);
    }

    public void setQuantity(int quantity){
        setText("x" + quantity);
        revalidate();
        repaint();
    }
}
