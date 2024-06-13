package it.polimi.ingsw.view.gui.scenes.localarea;

import javax.swing.*;
import java.awt.*;

public class PlaceHolder extends JComponent {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.fillOval(0,0,100,100);
    }

    @Override
    public Dimension getSize(Dimension rv) {
        return new Dimension(100,100);
    }

    @Override
    public int getWidth() {
        return 100;
    }

    @Override
    public int getHeight() {
        return 100;
    }
}
