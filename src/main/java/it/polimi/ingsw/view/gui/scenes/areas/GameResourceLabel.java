package it.polimi.ingsw.view.gui.scenes.areas;

import javax.swing.*;
//DOCS add docs
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
