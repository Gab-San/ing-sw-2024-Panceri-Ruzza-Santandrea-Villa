package it.polimi.ingsw.view.gui.scenes.setplayers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;

public class PlayerChoiceAction extends AbstractAction {
    public static final String PLAYERS_NUM_PROPERTY_NAME = "NUMBEROFPLAYERS";
    public PlayerChoiceAction(){
        super();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        JButton button = (JButton) e.getSource();
        String buttonText = button.getText();
        int numberOfPlayers = Integer.parseInt(buttonText.split("\\s+")[0]);
        firePropertyChange(PLAYERS_NUM_PROPERTY_NAME,0, numberOfPlayers);
        System.out.println(buttonText+ "\n" + numberOfPlayers);
        System.out.println(Arrays.toString(getPropertyChangeListeners()));
    }
}
