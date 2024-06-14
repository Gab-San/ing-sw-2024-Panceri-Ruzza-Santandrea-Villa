package it.polimi.ingsw.view.gui.scenes.dialogs.choosecolor;

import it.polimi.ingsw.PlayerColor;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ChooseColorAction extends AbstractAction {
    public static String CHOSEN_COLOR = "CHOSEN_COLOR";
    @Override
    public void actionPerformed(ActionEvent e) {
        this.setEnabled(false);
        JButton selectedButton = (JButton) e.getSource();
        String buttonColor = selectedButton.getText();
        char colorChar = buttonColor.charAt(0);
        firePropertyChange(CHOSEN_COLOR, null, colorChar);
    }
}
