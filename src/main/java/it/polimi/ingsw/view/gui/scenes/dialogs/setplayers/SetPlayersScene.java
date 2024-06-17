package it.polimi.ingsw.view.gui.scenes.dialogs.setplayers;

import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.GUIFunc;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;

public class SetPlayersScene extends JDialog implements GUI_Scene {
    private final JLabel setPlayerLabel;
    private final JButton twoPlayersButton;
    private final JButton threePlayersButton;
    private final JButton fourPlayersButton;
    private final JLabel errorLabel;
    private Timer errorTimer;
    public SetPlayersScene(Frame owner, String title, GameInputHandler inputHandler){
        // A modal dialog blocks all inputs on other windows
        super(owner, title, true);

        setLayout(new GridBagLayout());

        setPlayerLabel = new JLabel("Set number of players:");

        Action buttonAction = getButtonAction(inputHandler);
        twoPlayersButton = createSelectionButton(buttonAction, "2 Players");
        threePlayersButton = createSelectionButton(buttonAction, "3 Players");
        fourPlayersButton = createSelectionButton(buttonAction, "4 Players");

        errorLabel = createErrorLabel();

        // Adding components
        addGridComponent(setPlayerLabel, 0, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0,0,0,10),
                0,0);
        addGridComponent(twoPlayersButton, 1, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0),
                0,0);
        addGridComponent(threePlayersButton, 2, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0),
                0,0);
        addGridComponent(fourPlayersButton, 3,0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,0),
                0,0);
        addGridComponent(errorLabel, 1,1,2,1,1.0,1.0,GridBagConstraints.WEST,
                GridBagConstraints.VERTICAL, new Insets(0,0,0,0), 0,0);
    }

    @NotNull
    private Action getButtonAction(GameInputHandler inputHandler) {
        Action buttonAction = new PlayerChoiceAction();

        //When something happens to this action
        // it means it has been triggered.
        buttonAction.addPropertyChangeListener(evt -> {
            String propertyName = evt.getPropertyName();
            if(propertyName.equals(PlayerChoiceAction.PLAYERS_NUM_PROPERTY_NAME)){
                try {
                    inputHandler.setNumOfPlayers((Integer) evt.getNewValue());
                    close();
                } catch (RemoteException e) {
                    displayError("Connection lost!");
                    inputHandler.notifyDisconnection();
                }
            }
        });
        return buttonAction;
    }

    private JButton createSelectionButton(Action buttonAction, String buttonText){
        JButton button = new JButton(buttonAction);
        button.setText(buttonText);
        button.setFocusable(false);
        button.setVisible(true);
        return button;
    }

    private JLabel createErrorLabel(){
        JLabel errorLabel =  new JLabel("ERROR");
        errorLabel.setVisible(false);
        errorLabel.setForeground(GameColor.ERROR_COLOUR.getColor());
        return errorLabel;
    }

    private void addGridComponent(Component component, int x, int y, int width, int height, double weightx,
                                  double weighty, int anchor, int fill, Insets insets,
                                  int ipadx, int ipady){
        add(component, new GridBagConstraints(x, y, width, height, weightx, weighty, anchor, fill,
                insets, ipadx, ipady));
    }

    @Override
    public void display() {
        final int WIDTH = 500;
        final int HEIGHT = 200;
        GUIFunc.setupDialog(this, WIDTH, HEIGHT);
        setAllComponentsVisible();
        setVisible(true);
        requestFocus();
    }

    private void setAllComponentsVisible() {
        setPlayerLabel.setVisible(true);
        twoPlayersButton.setVisible(true);
        threePlayersButton.setVisible(true);
        fourPlayersButton.setVisible(true);
    }

    @Override
    public void displayError(String error) {
        if(errorTimer != null){
            errorTimer.stop();
        }
        errorTimer = new Timer(2500,
                event -> {
                    errorLabel.setText("");
                    errorLabel.setVisible(false);
                    errorTimer.stop();
                    errorTimer = null;
                    close();
                }
        );
        errorLabel.setText(error);
        errorLabel.setVisible(true);
        errorTimer.start();
    }

    @Override
    public void displayNotification(List<String> backlog) {

    }

    @Override
    public void close() {
        this.dispose();
    }
}
