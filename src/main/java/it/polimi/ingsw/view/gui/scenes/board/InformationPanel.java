package it.polimi.ingsw.view.gui.scenes.board;

import javax.swing.*;
import java.awt.*;

public class InformationPanel extends JPanel {
    private final int PANEL_WIDTH = 300;
    // Panel height doesn't affect the size since it counting for
    // the whole left side of the layout
    private final int PANEL_HEIGHT = 10;
    private ChatPanel chatPanel;
    private ScoreBoardPanel scoreBoardPanel;
    public InformationPanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.green);
        //region CHAT
        chatPanel = new ChatPanel();
        //endregion

        //region SCOREBOARD
        scoreBoardPanel = new ScoreBoardPanel();
        //endregion

        add(chatPanel, BorderLayout.PAGE_END);
        add(scoreBoardPanel, BorderLayout.CENTER);

        //Setting visibility
        chatPanel.setVisible(true);
        scoreBoardPanel.setVisible(true);
    }
}
