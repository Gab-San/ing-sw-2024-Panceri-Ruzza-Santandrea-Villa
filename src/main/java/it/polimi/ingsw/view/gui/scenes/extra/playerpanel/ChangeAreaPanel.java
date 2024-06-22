package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.gui.ChangeNotifications;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class implements a panel that lets the user know whether a player is connected or not
 * and navigate between the player areas.
 */
public class ChangeAreaPanel extends JPanel implements PropertyChangeListener {
    private final ChangeAreaButton areaButton;
    private final JLabel connectionLabel;
    private ImageIcon connectedIcon;
    private ImageIcon disconnectedIcon;

    /**
     * Constructs a player area panel.
     * @param areaName player nickname
     * @param action button action to navigate player areas
     * @param sceneID scene identifier of the player's play area
     * @param isConnected true if the player is connected, false otherwise
     */
    public ChangeAreaPanel(String areaName, Action action,
                           SceneID sceneID, boolean isConnected){
        this.areaButton = new ChangeAreaButton(action, sceneID);
        importIcons();
        areaButton.setText(areaName);

        areaButton.setFocusable(false);
        connectionLabel = setupConnectionLabel(areaName, isConnected);

        add(areaButton);
        add(connectionLabel);
    }

    private void importIcons() {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/Online_Icon_x16.png")){
            assert is != null;
            connectedIcon = new ImageIcon(ImageIO.read(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("icons/Offline_Icon_x16.png")){
            assert is != null;
            disconnectedIcon = new ImageIcon(ImageIO.read(is));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JLabel setupConnectionLabel(String areaName, boolean isConnected) {
        JLabel label = new JLabel();
        if(areaName.equalsIgnoreCase("board")){
            label.setText("");
        } else setLabelFormat(label, isConnected);
        return label;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(ChangeNotifications.CONNECTION_CHANGE)){
            assert evt.getNewValue() instanceof Boolean;
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
            if(evt.getNewValue() != null){
                Color playerColor = PlayerColor.getColor((PlayerColor) evt.getNewValue());
                areaButton.setBackground(playerColor);
                SwingUtilities.invokeLater(
                        this::repaint
                );
            }
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
                label.setIcon(disconnectedIcon);
            }
        }
    }

    /**
     * Returns the visible area button.
     * @return button associated with the displayed player's play area
     */
    public ChangeAreaButton getAreaButton(){
        return areaButton;
    }

    /**
     * Returns the name of the play area.
     * @return name of the play area
     */
    public String getAreaName(){
        return areaButton.getSceneName();
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(300, (int) (areaButton.getPreferredSize().getHeight() + 20));
    }
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(300, (int) areaButton.getPreferredSize().getHeight());
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, (int) (areaButton.getPreferredSize().getHeight() + 10));
    }
}
