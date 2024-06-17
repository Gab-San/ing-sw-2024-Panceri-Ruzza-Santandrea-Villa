package it.polimi.ingsw.view.gui.scenes.connection;

import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.List;

public class ConnectionScene extends JFrame implements GUI_Scene, KeyListener {
    // Frame settings
    private static final int LOGIN_FRAME_WIDTH = 500;
    private static final int LOGIN_FRAME_HEIGHT = 200;

    private final GameInputHandler inputHandler;
    // Components
    private final LoginPanel loginPanel;
    private final JLabel notificationLabel;
    private Timer displayTimer;


    public ConnectionScene(GameInputHandler inputHandler){
        setupFrame();
        //Setting layout
        this.setLayout(new BorderLayout());

        this.inputHandler = inputHandler;

        //Creating components
        loginPanel = new LoginPanel();
        //Creating border to distance panel from
        // the borders of the frame
        loginPanel.setBorder(BorderFactory.createEmptyBorder(25,0,10,0));
        loginPanel.addKeyListener(this);

        notificationLabel = GUIFunc.createNotificationLabel(200, 100, 300, 150);

        //Adding components
        add(loginPanel, BorderLayout.CENTER);
        add(notificationLabel, BorderLayout.SOUTH);

        loginPanel.setVisible(true);
    }

    private void setupFrame() {
        // Classic JFrame setup
        this.setSize(LOGIN_FRAME_WIDTH, LOGIN_FRAME_HEIGHT);
        // Defining center based on pack method. So this display shall be done
        // after packing the frame
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-this.getWidth()/2, -this.getHeight()/2);
        this.setLocation(center);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Login");
    }

    @Override
    public synchronized void display() {
        this.setVisible(true);
        // This method should make the window
        // appear in front of everything
        this.requestFocus();
    }

    @Override
    public void displayError(String error) {
        // Can be called only by timeout disconnection
        displayError(error, 1, false);
    }

    @Override
    public void displayNotification(List<String> backlog) {/*unused*/}

    @Override
    public synchronized void close() {
        // Closes the frame
        this.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {/*unused*/}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String nickname = loginPanel.getUserInput();
            try{
                loginPanel.disableInput();
                // Attempt of connection
                inputHandler.connect(nickname);
                // Here connection is successful
                displaySuccess("Login success!", 1.5f);
                // Goes to the next scene
                inputHandler.changeScene(SceneID.getMyAreaSceneID());
            } catch (IllegalStateException exc){
                loginPanel.enableInput();
                displayError(GUIFunc.correctToLabelFormat(exc.getMessage()), 2, false);
            } catch (RemoteException exc) {
                displayError(GUIFunc.correctToLabelFormat("Connection Lost!"), 1, true);
                inputHandler.notifyDisconnection();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {/*unused*/}

    private synchronized void displaySuccess(String successMessage, float displayTimeSeconds){
        int displayTime = GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        notificationLabel.setForeground(GameColor.NOTIFICATION_COLOUR.getColor());
        notificationLabel.setText(successMessage);
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime, true);
    }

    private void displayError(String errorMessage, float displayTimeSeconds, boolean close) {
        int displayTime =  GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        notificationLabel.setForeground(GameColor.ERROR_COLOUR.getColor());
        notificationLabel.setText(errorMessage);
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime, close);
    }

    private void startDisplayTimer(int displayTime, boolean isClosing) {
        // After delay time the notification will
        // disappear from the screen
        displayTimer = new Timer(displayTime,
                (event) -> {
                        notificationLabel.setVisible(false);
                        // java.awt timers don't stop after
                        // the delay time has passed,
                        // so they need to be actively stopped
                        displayTimer.stop();
                        displayTimer = null;
                        if(isClosing){
                            close();
                        }
                });
        displayTimer.start();
    }
}
