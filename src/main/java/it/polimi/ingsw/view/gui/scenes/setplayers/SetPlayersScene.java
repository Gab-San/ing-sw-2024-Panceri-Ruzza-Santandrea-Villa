package it.polimi.ingsw.view.gui.scenes.setplayers;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Scene;
import it.polimi.ingsw.view.gui.GUI_Scene;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SetPlayersScene extends JPanel implements GUI_Scene {

    private JButton twoPlayersButton;
    private JButton threePlayersButton;
    private JButton fourPlayersButton;

    public SetPlayersScene(){
        setSize(new Dimension(500, 200));
        setLayout(new GridBagLayout());

        JLabel setPlayerLabel = new JLabel("Set number of players:");
        twoPlayersButton = new JButton("2 Players");
        threePlayersButton = new JButton("3 Players");
        fourPlayersButton = new JButton("4 Players");

        addGridComponent(setPlayerLabel, 0, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL, new Insets(0,0,0,10),
                0,0);
        addGridComponent(twoPlayersButton, 0, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,10),
                0,0);
        addGridComponent(threePlayersButton, 0, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,10),
                0,0);
        addGridComponent(fourPlayersButton, 0, 0, 1, 1, 1.0, 1.0 ,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0,0,0,10),
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

    }
}
