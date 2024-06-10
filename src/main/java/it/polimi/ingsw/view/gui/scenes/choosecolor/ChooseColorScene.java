package it.polimi.ingsw.view.gui.scenes.choosecolor;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.ViewHand;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.List;

public class ChooseColorScene extends JDialog implements GUI_Scene, PropertyChangeListener {
    private final JButton redButton, yellowButton, greenButton, blueButton;
    private final JLabel notificationLabel;
    private Timer displayTimer;
    private final GameInputHandler inputHandler;

    public ChooseColorScene(Frame owner, String title, GameInputHandler inputHandler) {
        super(owner, title, true);
        this.inputHandler = inputHandler;
        setLayout(new GridBagLayout());
        Action colorButtonAction = getButtonAction();
        redButton = createColorButton( colorButtonAction, "Red", Color.red);
        yellowButton = createColorButton(colorButtonAction,"Yellow", Color.yellow);
        blueButton = createColorButton(colorButtonAction,"Blue", Color.BLUE);
        greenButton = createColorButton(colorButtonAction,"Green", Color.GREEN);

        notificationLabel = GUIFunc.createNotificationLabel();

        // Adding components
        addGridComponent(redButton, 0,0,1,1,1.0,1.0,GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0);
        addGridComponent(yellowButton, 1,0,1,1,1.0,1.0,GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0);
        addGridComponent(blueButton, 2,0,1,1,1.0,1.0,GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0);
        addGridComponent(greenButton, 3,0,1,1,1.0,1.0,GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(0,0,0,0), 0,0);
        addGridComponent(notificationLabel,1,1,2,1,1.0,1.0,GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, new Insets(0,20,0,0), 0, 0);

    }

    private JLabel createErrorLabel() {
        JLabel errorLabel =  new JLabel("ERROR");
        errorLabel.setVisible(false);
        //TODO choose better color
        errorLabel.setForeground(Color.red);
        return errorLabel;
    }

    private void addGridComponent(Component component, int x, int y, int width, int height, double weightx,
                                  double weighty, int anchor, int fill, Insets insets,
                                  int ipadx, int ipady){
        add(component, new GridBagConstraints(x, y, width, height, weightx, weighty, anchor, fill,
                insets, ipadx, ipady));
    }

    @NotNull
    private Action getButtonAction() {
        Action colorButtonAction = new ChooseColorAction();
        colorButtonAction.addPropertyChangeListener(
                evt -> {
                    if(evt.getPropertyName().equals(ChooseColorAction.CHOSEN_COLOR)){
                        try {
                            inputHandler.chooseColor((Character) evt.getNewValue());
                            close();
                        } catch (RemoteException e) {
                            displayError("Connection Lost!");
                            inputHandler.notifyDisconnection();
                        }
                    }
                }
        );
        return colorButtonAction;
    }

    private JButton createColorButton(Action action, String colorName, Color buttonColor) {
        JButton colorButton = new JButton(action);
        colorButton.setText(colorName);
        //FIXME adjust colors
//        colorButton.setForeground(Color.white);
        colorButton.setBackground(buttonColor);

        colorButton.setPreferredSize(new Dimension(100, 40));

        colorButton.setVisible(true);
        colorButton.setFocusable(false);
        return colorButton;
    }

    @Override
    public synchronized void display() {
        final int WIDTH = 700;
        final int HEIGHT = 300;
        GUIFunc.setupDialog(this, WIDTH, HEIGHT);
        setAllComponentsVisible();
        setVisible(true);
    }

    private void setAllComponentsVisible() {
        redButton.setVisible(true);
        blueButton.setVisible(true);
        greenButton.setVisible(true);
        yellowButton.setVisible(true);
    }

    @Override
    public synchronized void displayError(String error) {
        // Can be called only by timeout disconnection
        displayError(error, 1, false);
    }

    @Override
    public synchronized void displayNotification(List<String> backlog) {

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
    public synchronized void close() {
        this.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        assert evt.getSource() instanceof ViewHand;
        ViewHand playerHand = (ViewHand) evt.getSource();

        if(inputHandler.isLocalPlayer(playerHand.getNickname())){
            System.err.println("CLOSING WINDOW");
            SwingUtilities.invokeLater(
                    this::close
            );
            return;
        }
        SwingUtilities.invokeLater(
                () -> displayNotification(playerHand.getNickname() + " chose color " + evt.getNewValue(), 1500)
        );
        PlayerColor color = (PlayerColor) evt.getNewValue();
        SwingUtilities.invokeLater(
                () -> {
                    switch (color) {
                        case BLUE:
                            blueButton.setEnabled(false);
                            blueButton.setAction(null);
                            blueButton.setVisible(false);
                            break;
                        case YELLOW:
                            yellowButton.setEnabled(false);
                            yellowButton.setAction(null);
                            yellowButton.setVisible(false);
                            break;
                        case GREEN:
                            greenButton.setEnabled(false);
                            greenButton.setAction(null);
                            greenButton.setVisible(false);
                            break;
                        case RED:
                            redButton.setEnabled(false);
                            redButton.setAction(null);
                            redButton.setVisible(false);
                            break;
                    }
                    System.out.println("SHOULD BE REVALIDATING");
                    revalidate();
                    repaint();
                }
        );
    }


    private synchronized void displayError(String errorMessage, float displayTimeSeconds, boolean close){
        int displayTime =  GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        //TODO [Gamba] Fix color
        notificationLabel.setForeground(Color.red);
        notificationLabel.setText(errorMessage);
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime, close);
    }

    private synchronized void displayNotification(String notificationMsg, float displayTimeSeconds){
        int displayTime = GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        //TODO [Gamba] Fix color
        notificationLabel.setForeground(Color.green);
        notificationLabel.setText(notificationMsg);
        notificationLabel.setVisible(true);
        SwingUtilities.invokeLater(
                () -> startDisplayTimer(displayTime, true)
        );
        revalidate();
        repaint();
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
