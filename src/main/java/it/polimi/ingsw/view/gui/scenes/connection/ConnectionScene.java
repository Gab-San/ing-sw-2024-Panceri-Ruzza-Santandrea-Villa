package it.polimi.ingsw.view.gui.scenes.connection;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.util.List;

public class ConnectionScene extends JFrame implements GUI_Scene, KeyListener {
    // TODO: this is horrible

    // Settings
    private final int LOGIN_FRAME_WIDTH = 500;
    private final int LOGIN_FRAME_HEIGHT = 200;

    private final GameInputHandler inputHandler;
    // Components
    private final LoginPanel loginPanel;
    private final JLabel notificationLabel;
    private Timer displayTimer;
    private final GUI gui;

    public ConnectionScene(GameInputHandler inputHandler, GUI gui){
        this.gui = gui;
        setupFrame();
        //Setting layout
        this.setLayout(new BorderLayout());

        this.inputHandler = inputHandler;

        //Creating components
        loginPanel = new LoginPanel();
        loginPanel.setBorder(BorderFactory.createEmptyBorder(25,0,10,0));
        loginPanel.addKeyListener(this);

        notificationLabel = createErrorLabel();

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
    public void display() {
        this.setVisible(true);
        this.requestFocus();
    }

    @Override
    public void displayError(String error) {
        displayError(error, 1);
    }

    @Override
    public void displayNotification(List<String> backlog) {/*unused*/}

    @Override
    public void displayChatMessage(List<String> backlog) {/*unused*/}

    @Override
    public void moveView(List<CornerDirection> cornerDirections) {/*unused*/}

    @Override
    public void setCenter(int row, int col) {/*unused*/}

    @Override
    public void setCenter(GamePoint center) {/*unused*/}

    @Override
    public void close() {
        this.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {/*unused*/}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            String nickname = loginPanel.getUserInput();
            try{
                inputHandler.connect(nickname);
                displaySuccess("Login success!", 1.5f);
                gui.displayNextScene(SceneID.getMyAreaSceneID());
            } catch (IllegalStateException exc){
                displayError(correctToLabelFormat(exc.getMessage()), 2);
            } catch (RemoteException exc) {
                displayError(correctToLabelFormat(exc.getMessage()), 1);
                inputHandler.notifyDisconnection();
            }
        }
    }

    private String correctToLabelFormat(String message) {
        return "<html>" + message.replaceAll("\n", "<br>") + "</html>";
    }

    @Override
    public void keyReleased(KeyEvent e) {/*unused*/}


    private JLabel createErrorLabel(){
        JLabel label =  new JLabel("");
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setPreferredSize(new Dimension(200,100));
        label.setMaximumSize(new Dimension(300,150));
        label.setVisible(false);
        return label;
    }

    private void displayError(String errorMessage, float displayTimeSeconds){
        int displayTime = setupDisplayTimer(displayTimeSeconds);
        //TODO [Gamba] Fix color
        notificationLabel.setForeground(Color.red);
        notificationLabel.setText(errorMessage);
        notificationLabel.setVisible(true);
        this.setVisible(true);
        startDisplayTimer(displayTime, false);
    }

    private void displaySuccess(String successMessage, float displayTimeSeconds){
        int displayTime = setupDisplayTimer(displayTimeSeconds);
        //TODO [Gamba] Fix color
        notificationLabel.setForeground(Color.green);
        notificationLabel.setText(successMessage);
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime, true);
    }

    private void startDisplayTimer(int displayTime, boolean isClosing) {
        displayTimer = new Timer(displayTime,
                (event) -> {
                        notificationLabel.setVisible(false);
                        displayTimer.stop();
                        displayTimer = null;
                        if(isClosing){
                            close();
                        }
                });
        displayTimer.start();
    }

    private int setupDisplayTimer(float displayTimeSeconds){
        int displayTime = (int) (displayTimeSeconds * 1000);
        if(displayTimer != null){
            displayTimer.stop();
        }
        return displayTime;
    }


}
