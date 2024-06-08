package it.polimi.ingsw.view.gui.scenes.connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPanel extends JPanel {
    private final JTextField loginTextField;

    public LoginPanel() {

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
                loginTextField.setText("");
                loginTextField.setFocusable(true);
                loginTextField.grabFocus();
            }
        });

        loginTextField.setBorder(BorderFactory.createEtchedBorder());

        JLabel loginLabel = new JLabel("Username");

        add(loginLabel);
        add(loginTextField);
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        loginTextField.addKeyListener(l);
    }

    public String getUserInput() {
        return loginTextField.getText();
    }
}
