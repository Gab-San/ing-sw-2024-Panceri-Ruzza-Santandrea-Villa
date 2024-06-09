package it.polimi.ingsw.view.gui.scenes.choosecolor;

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
        System.out.println("CHOSEN COLOR: "+ PlayerColor.parseColour(colorChar));
        firePropertyChange(CHOSEN_COLOR, null, colorChar);
    }
}
