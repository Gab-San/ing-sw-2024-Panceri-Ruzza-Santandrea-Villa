package it.polimi.ingsw.view.gui.scenes.dialogs.setplayers;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PlayerChoiceAction extends AbstractAction {
    public static final String PLAYERS_NUM_PROPERTY_NAME = "NUMBER_OF_PLAYERS";
    public PlayerChoiceAction(){
        super();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        JButton button = (JButton) e.getSource();
        String buttonText = button.getText();
        int numberOfPlayers = Integer.parseInt(buttonText.split("\\s+")[0]);
        // Changes the property of this action in order to be caught
        // by the scene. It makes it possible to bounce the information between
        // the buttons and the frame in order to distinct the components.
        firePropertyChange(PLAYERS_NUM_PROPERTY_NAME,0, numberOfPlayers);
    }
}
