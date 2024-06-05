package it.polimi.ingsw.view.gui.scenes.connection;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

/**
 * This scene is the first screen the user encounters. It displays information to login into the game
 * and handles login for that login.
 */
public class ConnectionScene extends JPanel implements Scene{
    private final int SCREEN_WIDTH = 500;
    private final int HEIGHT_WIDTH = 200;

    public ConnectionScene(){

        //Setting window size information
        //Setting fixed until know how to make resizable screen
        setSize(SCREEN_WIDTH,HEIGHT_WIDTH);
    }

    @Override
    public void display() {
        repaint();
        setVisible(true);
    }

    @Override
    public void displayError(String error) {
        JOptionPane.showMessageDialog(
                this,
                error,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    @Override
    public void displayNotification(List<String> backlog) {

    }

    @Override
    public void displayChatMessage(List<String> backlog) {

    }

    @Override
    public void moveView(List<CornerDirection> cornerDirections) {

    }

    @Override
    public void setCenter(int row, int col) {

    }

    @Override
    public void setCenter(GamePoint center) {

    }


    /**
     * Nickname must contain at least one letter and neither start nor end with a space <br>
     * It can contain any character, minimum length of 3 characters
     * @param nickname nickname to validate
     * @return true if the nickname is valid, <br> false if not valid
     */
    private boolean validateNickname(String nickname){
        return nickname.matches("[^\n ].*[a-zA-Z].*[^\n ]")
                && nickname.length() < Client.MAX_NICKNAME_LENGTH;
    }
}
