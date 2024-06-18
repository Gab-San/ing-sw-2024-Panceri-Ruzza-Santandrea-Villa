package it.polimi.ingsw.view.gui.scenes.areas;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This class represents a gui action. It moves the screen in the selected direction.
 */
public class MoveScreenAction extends AbstractAction {
    private final int directionX;
    private final int directionY;

    /**
     * Constructs the movement action in the desired direction.
     * <p>
     *     - (1,0) RIGHT; <br>
     *     - (-1,0) LEFT; <br>
     *     - (0,1) DOWN; <br>
     *     - (0,-1) UP;
     * </p>
     * @param directionX x axis direction
     * @param directionY y axis direction
     */
    public MoveScreenAction(int directionX, int directionY){
        this.directionX = directionX;
        this.directionY = directionY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        assert e.getSource() instanceof JScrollPane;
        JScrollPane scrollPane = (JScrollPane) e.getSource();
        if(directionX != 0) {
            JScrollBar hBar = scrollPane.getHorizontalScrollBar();
            int xOffset = hBar.getValue() + directionX * hBar.getBlockIncrement();
            hBar.setValue(xOffset);
        }
        if(directionY != 0) {
            JScrollBar vBar = scrollPane.getVerticalScrollBar();
            int yOffset = vBar.getValue() + directionY * vBar.getBlockIncrement();
            vBar.setValue(yOffset);
        }
    }
}
