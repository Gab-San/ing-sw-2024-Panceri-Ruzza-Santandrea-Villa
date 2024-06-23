package it.polimi.ingsw.view.gui.scenes.dialogs.choosecolor;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.GameColor;
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

import static it.polimi.ingsw.view.GameColor.ERROR_COLOUR;
import static it.polimi.ingsw.view.GameColor.NOTIFICATION_COLOUR;

/**
 * This class implements a dialog to choose the player color.
 */
public class ChooseColorScene extends JDialog implements GUI_Scene, PropertyChangeListener {
    private final JButton redButton, yellowButton, greenButton, blueButton;
    private final JLabel notificationLabel;
    private Timer displayTimer;
    private final GameInputHandler inputHandler;

    /**
     * Constructs choose color dialog.
     * @param owner frame this dialog is displayed on
     * @param title dialog title
     * @param inputHandler user input handler
     */
    public ChooseColorScene(Frame owner, String title, GameInputHandler inputHandler) {
        super(owner, title, true);
        this.inputHandler = inputHandler;
        setLayout(new GridBagLayout());
        Action colorButtonAction = getButtonAction();
        redButton = createColorButton( colorButtonAction, "Red", GameColor.PLAYER_RED.getColor());
        yellowButton = createColorButton(colorButtonAction,"Yellow", GameColor.PLAYER_YELLOW.getColor());
        blueButton = createColorButton(colorButtonAction,"Blue", GameColor.PLAYER_BLUE.getColor());
        greenButton = createColorButton(colorButtonAction,"Green", GameColor.PLAYER_GREEN.getColor());

        notificationLabel = GUIFunc.createNotificationLabel();

        // Adding components
        addGridComponent(redButton, 0,0,1,
                GridBagConstraints.NONE, new Insets(0,0,0,0));
        addGridComponent(yellowButton, 1,0,1,
                GridBagConstraints.NONE, new Insets(0,0,0,0));
        addGridComponent(blueButton, 2,0,1,
                GridBagConstraints.NONE, new Insets(0,0,0,0));
        addGridComponent(greenButton, 3,0,1,
                GridBagConstraints.NONE, new Insets(0,0,0,0));
        addGridComponent(notificationLabel,1,1,2,
                GridBagConstraints.VERTICAL, new Insets(0,20,0,0));

    }

    private void addGridComponent(Component component, int x, int y, int width,
                                  int fill, Insets insets){
        add(component, new GridBagConstraints(x, y, width, 1, 1.0, 1.0, GridBagConstraints.CENTER, fill,
                insets, 0, 0));
    }

    @NotNull
    private Action getButtonAction() {
        Action colorButtonAction = new ChooseColorAction();
        colorButtonAction.addPropertyChangeListener(
                evt -> {
                    if(evt.getPropertyName().equals(ChooseColorAction.CHOSEN_COLOR)){
                        try {
                            inputHandler.chooseColor((Character) evt.getNewValue());
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
    public synchronized void displayError(String errorMessage) {
        // Can be called only by timeout disconnection
        // Or if an error occurs while choosing color
        int displayTime =  GUIFunc.setupDisplayTimer((float) 1, displayTimer);
        notificationLabel.setForeground(ERROR_COLOUR.getColor());
        notificationLabel.setText(errorMessage);
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }

    @Override
    public synchronized void displayNotification(List<String> backlog) {/*unused*/}

    @Override
    public synchronized void close() {
        this.dispose();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        assert evt.getSource() instanceof ViewHand;
        ViewHand playerHand = (ViewHand) evt.getSource();

        if(inputHandler.isLocalPlayer(playerHand.getNickname())){
            SwingUtilities.invokeLater(
                    this::close
            );
            return;
        }

        SwingUtilities.invokeLater(
                () -> displayNotification(playerHand.getNickname() + " chose color " + evt.getNewValue())
        );

        // Disables and hides buttons of colors already chosen.
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
                    revalidate();
                    repaint();
                }
        );
    }



    private synchronized void displayNotification(String notificationMsg){
        int displayTime = GUIFunc.setupDisplayTimer((float) 1500, displayTimer);
        notificationLabel.setForeground(NOTIFICATION_COLOUR.getColor());
        notificationLabel.setText(notificationMsg);
        notificationLabel.setVisible(true);
        SwingUtilities.invokeLater(
                () -> startDisplayTimer(displayTime)
        );
        revalidate();
        repaint();
    }

    private void startDisplayTimer(int displayTime) {
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
                });
        displayTimer.start();
    }
}
