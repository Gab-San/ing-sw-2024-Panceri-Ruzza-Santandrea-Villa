package it.polimi.ingsw.view.gui.scenes.extra;

import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GameInputHandler;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerListPanel extends JPanel implements PropertyChangeListener{
    private final GameInputHandler inputHandler;
    public PlayerListPanel(GameInputHandler inputHandler){
        this.inputHandler = inputHandler;
        setPreferredSize(new Dimension(300,200));
        setBackground(Color.pink);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
