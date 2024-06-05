package it.polimi.ingsw.view.gui.scenes.board;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    private final int CHAT_WIDTH = 300;
    private final int CHAT_HEIGHT = 400;
    public ChatPanel(){
        setBackground(new Color(0xbeefe5));
        setPreferredSize(new Dimension(CHAT_WIDTH,CHAT_HEIGHT));
    }
}
