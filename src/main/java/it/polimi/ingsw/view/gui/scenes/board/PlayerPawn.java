package it.polimi.ingsw.view.gui.scenes.board;

import it.polimi.ingsw.view.gui.GameWindow;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PlayerPawn extends JComponent {
    private final Color pawnColor;
    private final int width = (int) (20 * GameWindow.SCALE_FACTOR);
    private final int height = (int) (20 * GameWindow.SCALE_FACTOR);
    public PlayerPawn(Color pawnColor){
        this.pawnColor = pawnColor;
    }

    public Color getColor(){
        return pawnColor;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(20,20);
    }

    @Override
    public Dimension getSize() {
        return new Dimension(20,20);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setPaint(pawnColor);
        graphics2D.fillOval(0,0, width,height);
    }
}
