package it.polimi.ingsw.view.gui.scenes.connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorPanel extends JPanel {
    private final JLabel errorLabel;
    private Timer errorTimer;

    public ErrorPanel() {
        // By using this layout (which default setting is centering)
        // the label will be automatically centered
        this.setLayout(new FlowLayout());
        this.setMinimumSize(new Dimension(100,20));

        errorLabel = new JLabel("ERROR");
        // Foreground in this case is the text
        //TODO: Fix color
        errorLabel.setForeground(Color.red);
        //Later it will be made visible
        errorLabel.setVisible(false);
        this.add(errorLabel);
        this.setVisible(true);
    }

    public void displayError(String errorMessage, int displayTimeSeconds){
        int displayTime = displayTimeSeconds * 1000;
        if(errorTimer != null){
            errorTimer.stop();
        }
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        this.setVisible(true);
        errorTimer = new Timer(displayTime,
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        errorLabel.setVisible(false);
                        setVisible(false);
                        errorTimer.stop();
                        errorTimer = null;
                    }
                });
    }
}
