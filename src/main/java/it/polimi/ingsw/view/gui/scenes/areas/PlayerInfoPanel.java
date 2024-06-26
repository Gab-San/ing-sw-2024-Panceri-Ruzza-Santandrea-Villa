package it.polimi.ingsw.view.gui.scenes.areas;

import it.polimi.ingsw.GameResource;
import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.ChangeNotifications;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

/**
 * This class represents the panel that displays all the information about player status.
 * <p>
 *     It is comprehensive of: <br>
 *     - visible resources; <br>
 *     - deadlock; <br>
 *     - player's turn; <br>
 *     - current turn.
 * </p>
 */
public class PlayerInfoPanel extends JPanel implements PropertyChangeListener {
    /**
     * Preferred height of the panel.
     */
    public static int HEIGHT = 40;
    private final VisibleResourcesPanel visibleResourcesPanel;
    private final JLabel deadLockLabel;
    private final JLabel turnLabel;
    private final JLabel currentTurnLabel;

    /**
     * Constructs the player info panel with the specified deadlock status and assigned turn.
     * The latter might be 0 as it is the standard value for initialized and unassigned turn.
     * @param isDeadLocked true if the player cannot play any more cards, false otherwise
     * @param turn player assigned turn
     */
    public PlayerInfoPanel(boolean isDeadLocked, int turn){
        setLayout(new FlowLayout(FlowLayout.CENTER));
        // x dimension doesn't count since it's a component into the BorderLayout set to the north
        setPreferredSize(new Dimension(getWidth(), HEIGHT));
        visibleResourcesPanel = new VisibleResourcesPanel();
        deadLockLabel = new JLabel("Deadlock: " + isDeadLocked);
        deadLockLabel.setBorder(BorderFactory.createEmptyBorder(0,15,15,15));
        turnLabel = new JLabel("Player Turn: " + turn);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(0,15,15,15));
        currentTurnLabel = new JLabel("Current Turn: " + 0);
        currentTurnLabel.setForeground(GameColor.CURRENT_TURN_COLOR.getColor());
        currentTurnLabel.setBorder(BorderFactory.createEmptyBorder(0,15,15,15));
        add(visibleResourcesPanel);
        add(deadLockLabel);
        add(turnLabel);
        add(currentTurnLabel);
    }

    /**
     * Displays player's deadlock status.
     * @param deadLocked true if the player cannot play any more cards, false otherwise
     */
    public void setDeadLocked(boolean deadLocked) {
        deadLockLabel.setText("Deadlock: " + deadLocked);
        SwingUtilities.invokeLater(
                () -> {
                    revalidate();
                    repaint();
                }
        );
    }

    /**
     * Displays player's playing turn.
     * @param turn assigned turn
     */
    public void setTurn(int turn){
        turnLabel.setText("Player Turn: " + turn);
        SwingUtilities.invokeLater(
                () -> {
                    revalidate();
                    repaint();
                }
        );
    }

    /**
     * Adjourns the label to display current turn.
     * @param currentTurn general playing turn
     */
    public void adjournCurrentTurn(int currentTurn){
        currentTurnLabel.setText("Current Turn: " + currentTurn);
        SwingUtilities.invokeLater(
                () ->{
                    revalidate();
                    repaint();
                }
        );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case ChangeNotifications.VIS_RES_CHANGE:
                assert evt.getNewValue() instanceof Map;

                Map<GameResource, Integer> resourceIntegerMap = (Map<GameResource, Integer>) evt.getNewValue();
                if(resourceIntegerMap.isEmpty()) visibleResourcesPanel.clear();
                //If empty this foreach loop will be jumped
                for(GameResource res : resourceIntegerMap.keySet()){
                    visibleResourcesPanel.adjournResourceCount(res, resourceIntegerMap.get(res));
                }
                SwingUtilities.invokeLater(
                        () -> {
                            revalidate();
                            repaint();
                        }
                );
                break;
            case ChangeNotifications.CURRENT_TURN_UPDATE:
                assert evt.getNewValue() instanceof Integer;
                adjournCurrentTurn((Integer) evt.getNewValue());
                break;
            case ChangeNotifications.PLAYER_TURN_UPDATE:
                assert evt.getNewValue() instanceof Integer;
                setTurn((Integer) evt.getNewValue());
                break;
            case ChangeNotifications.PLAYER_DEADLOCK_UPDATE:
                assert evt.getNewValue() instanceof Boolean;
                setDeadLocked((Boolean) evt.getNewValue());
                break;
        }
    }
}
