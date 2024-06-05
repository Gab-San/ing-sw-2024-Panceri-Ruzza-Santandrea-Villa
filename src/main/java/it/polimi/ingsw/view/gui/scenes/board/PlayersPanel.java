package it.polimi.ingsw.view.gui.scenes.board;

import javax.swing.*;
import java.awt.*;

public class PlayersPanel extends JPanel {
    private final int PANEL_WIDTH = 100;
    // Panel height doesn't affect the size since it counting for
    // the whole left side of the layout
    private final int PANEL_HEIGHT = 10;
    public PlayersPanel(){
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.red);
    }
}
