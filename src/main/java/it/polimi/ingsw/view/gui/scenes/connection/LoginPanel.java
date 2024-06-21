package it.polimi.ingsw.view.gui.scenes.connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This class represents a login panel. It contains all the components to
 * effectively display a panel that can take as input any string and
 * tries to connect to the main lobby using the string as username.
 */
public class LoginPanel extends JPanel {
    private final JTextField loginTextField;

    /**
     * Default constructor.
     */
    public LoginPanel() {
        //Flow layout is always displayed on top
        this.setLayout(new FlowLayout());
        // Text field is responsible for taking user input as string
        // it will then pass it to the input controller when the user hits
        // enter
        loginTextField = new JTextField("Insert nickname here...", 20);
        // By making it not focusable the user has to click on it to grab focus
        // this means that it is assured in order to write that the mouse clicked action
        // has occurred
        loginTextField.setFocusable(false);
        loginTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!loginTextField.isFocusable()) {
                    loginTextField.setText("");
                    //Makes it possible to write
                    loginTextField.setFocusable(true);
                    loginTextField.grabFocus();
                }
            }
        });

        loginTextField.setBorder(BorderFactory.createEtchedBorder());

        JLabel loginLabel = new JLabel("Username");

        add(loginLabel);
        add(loginTextField);
    }

    @Override
    public void addKeyListener(KeyListener l) {
        loginTextField.addKeyListener(l);
    }

    /**
     * Returns the input string written by the user in the corresponding field.
     * @return user input string
     */
    public String getUserInput() {
        return loginTextField.getText();
    }

    /**
     * Disables text input.
     */
    public void disableInput() {
        loginTextField.setEnabled(false);
    }

    /**
     * Enables text inpu.
     */
    public void enableInput() {
        loginTextField.setEnabled(true);
    }
}
