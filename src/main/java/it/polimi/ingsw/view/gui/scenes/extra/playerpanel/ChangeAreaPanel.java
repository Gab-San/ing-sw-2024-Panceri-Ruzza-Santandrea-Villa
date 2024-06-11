package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ChangeAreaPanel extends JPanel implements PropertyChangeListener {
    private final ChangeAreaButton areaButton;
    private final JLabel connectionLabel;
    private ImageIcon connectedIcon;
    private ImageIcon disconnectedIcon;

    public ChangeAreaPanel(String areaName, Action action,
                           SceneID sceneID, boolean isConnected){
        this.areaButton = new ChangeAreaButton(action, sceneID);
        areaButton.setSceneName(areaName);
        areaButton.setFocusable(false);
        //TODO: add icon
        connectionLabel = setupConnectionLabel(areaName, isConnected);

        add(areaButton);
        add(connectionLabel);
    }

    private JLabel setupConnectionLabel(String areaName, boolean isConnected) {
        JLabel label = new JLabel();
        if(areaName.equalsIgnoreCase("board")){
            label.setText("BOARD");
        } else setLabelFormat(label, isConnected);
        return label;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(ChangeNotifications.CONNECTION_CHANGE)){
            assert evt.getNewValue() instanceof Boolean;
            System.out.println("CONNECTION CHANGED: " + ((ViewHand) evt.getSource()).getNickname());
            boolean connectionStatus = (boolean) evt.getNewValue();
            setLabelFormat(connectionLabel, connectionStatus);
            SwingUtilities.invokeLater(
                    () ->{
                        this.revalidate();
                        this.repaint();
                    }
            );
            return;
        }

        if(evt.getPropertyName().equals(ChangeNotifications.COLOR_CHANGE)){
            assert evt.getNewValue() instanceof PlayerColor;
            System.out.println("COLOR CHANGE BY: " + ((ViewHand) evt.getSource()).getNickname());
            Color playerColor = PlayerColor.getColor((PlayerColor) evt.getNewValue());
            areaButton.setBackground(playerColor);
            SwingUtilities.invokeLater(
                this::repaint
            );
        }
    }

    private void setLabelFormat(JLabel label, boolean connectionStatus) {
        if(connectionStatus){
            if(connectedIcon == null) {
                label.setText("ONLINE");
                label.setForeground(Color.green);
            } else {
                label.setIcon(connectedIcon);
            }
        } else{
            if(disconnectedIcon == null) {
                label.setText("OFFLINE");
                label.setForeground(Color.red);
            } else {
                label.setIcon(connectedIcon);
            }
        }
    }

    public ChangeAreaButton getAreaButton(){
        return areaButton;
    }

    public void setIconImages(ImageIcon connectedIcon, ImageIcon disconnectedIcon){
        this.connectedIcon = connectedIcon;
        this.disconnectedIcon = disconnectedIcon;
    }

    public String getAreaName(){
        return areaButton.getSceneName();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(300, 30);
    }
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(300, 20);
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 25);
    }
}
