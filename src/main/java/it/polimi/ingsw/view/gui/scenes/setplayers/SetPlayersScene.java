package it.polimi.ingsw.view.gui.scenes.setplayers;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.List;

public class SetPlayersScene extends JFrame implements GUI_Scene {
    private final JLabel setPlayerLabel;
    private final JButton twoPlayersButton;
    private final JButton threePlayersButton;
    private final JButton fourPlayersButton;
    public SetPlayersScene(GameInputHandler inputHandler){

        setLayout(new GridBagLayout());

        setPlayerLabel = new JLabel("Set number of players:");

        Action buttonAction = new PlayerChoiceAction();

        //When something happens to this action
        // it means it has been triggered.
        buttonAction.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if(propertyName.equals(PlayerChoiceAction.PLAYERS_NUM_PROPERTY_NAME)){
                    try {
                        inputHandler.setNumOfPlayers((Integer) evt.getNewValue());
                    } catch (RemoteException e) {
                        inputHandler.notifyDisconnection();
                    }
                }
                close();
            }
        });
        twoPlayersButton = new JButton(buttonAction);
        threePlayersButton = new JButton(buttonAction);
        fourPlayersButton = new JButton(buttonAction);

        twoPlayersButton.setText("2 Players");
        threePlayersButton.setText("3 Players");
        fourPlayersButton.setText("4 Players");

        twoPlayersButton.setFocusable(false);
        threePlayersButton.setFocusable(false);
        fourPlayersButton.setFocusable(false);

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
        setSize(new Dimension(WIDTH, HEIGHT));
        Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        center.translate(-WIDTH/2, -HEIGHT/2);
        setLocation(center);
        //FIXME: for now it can exit since join should ping the client
        // but it should first send a notification to the server
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPlayerLabel.setVisible(true);
        twoPlayersButton.setVisible(true);
        threePlayersButton.setVisible(true);
        fourPlayersButton.setVisible(true);
        setVisible(true);
        requestFocus();
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
    public void close() {
        this.dispose();
    }
}
