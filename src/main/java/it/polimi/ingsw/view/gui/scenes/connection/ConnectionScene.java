package it.polimi.ingsw.view.gui.scenes.connection;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.List;

public class ConnectionScene extends JFrame implements Scene, KeyListener {

    // Settings
    private final int LOGIN_FRAME_WIDTH = 500;
    private final int LOGIN_FRAME_HEIGHT = 200;

    private final GameInputHandler inputHandler;
    // Components
    private final ErrorPanel errorPanel;
    private final LoginPanel loginPanel;

    public ConnectionScene(GameInputHandler inputHandler){
        // Classic JFrame setup
        this.setSize(LOGIN_FRAME_WIDTH, LOGIN_FRAME_HEIGHT);
        // Defining center based on pack method. So this display shall be done
        // after packing the frame
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-this.getWidth()/2, -this.getHeight()/2);
        this.setLocation(center);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Login");
        this.setLayout(new BorderLayout());

        this.inputHandler = inputHandler;

        //Creating components
        loginPanel = new LoginPanel();
//        loginPanel.setBorder(BorderFactory.createEmptyBorder(20,0,10,0));
        loginPanel.addKeyListener(this);
        errorPanel = new ErrorPanel();
        //Adding components
        add (loginPanel, BorderLayout.CENTER);
        add(errorPanel, BorderLayout.SOUTH);

        loginPanel.setVisible(true);
        errorPanel.setVisible(true);
    }

    @Override
    public void display() {
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void displayError(String error) {

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

    @Override
    public void keyTyped(KeyEvent e) {/*unused*/}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String nickname = loginPanel.getUserInput();
            try{
                inputHandler.connect(nickname);
            } catch (IllegalStateException exc){
                errorPanel.displayError(exc.getMessage(), 2);
            } catch (RemoteException ex) {
                inputHandler.notifyDisconnection();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {/*unused*/}
}
