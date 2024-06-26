package it.polimi.ingsw.view.gui.scenes.dialogs.choosecolor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * This class is an action that informs the input handler about the color chosen by the user.
 */
public class ChooseColorAction extends AbstractAction {
    /**
     * Default constructor.
     */
    public ChooseColorAction(){}
    /**
     * Choose color action property name.
     */
    public static String CHOSEN_COLOR = "CHOSEN_COLOR";

    /**
     * Invoked when one of the available colors is chosen, attempts to send the choice to the network.
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton selectedButton = (JButton) e.getSource();
        String buttonColor = selectedButton.getText();
        char colorChar = buttonColor.charAt(0);
        firePropertyChange(CHOSEN_COLOR, null, colorChar);
    }
}
