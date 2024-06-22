package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewOpponentHand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class implements a player panel on which the player in lobby are displayed.
 * It also allows for the user to swap between player areas.
 */
public class PlayerListPanel extends JPanel implements PropertyChangeListener{
    /**
     * Panel preferred width.
     */
    public static int WIDTH = 300;
    /**
     * Panel preferred height.
     */
    public static int HEIGHT = 200;
    private final GameInputHandler inputHandler;
    private final ButtonGroup buttonPlayerGroup;
    private final ChangeAreaAction buttonAction;
    private final List<ChangeAreaPanel> areaList;

    /**
     * Constructs the players panel.
     * @param inputHandler game interaction handler
     */

    public PlayerListPanel(GameInputHandler inputHandler){
        this.inputHandler = inputHandler;
        this.areaList = new LinkedList<>();
        setPreferredSize(new Dimension(WIDTH,HEIGHT));
        setLayout(new GridLayout(0,1));

        buttonAction = setupButtonAction();
        ChangeAreaPanel boardArea = new ChangeAreaPanel("Board", buttonAction, SceneID.getBoardSceneID(), true);
        buttonPlayerGroup = setupButtonGroup(new ArrayList<>(List.of(new ChangeAreaButton[]{boardArea.getAreaButton()})));
        boardArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(boardArea);

    }


    private ChangeAreaAction setupButtonAction() {
        ChangeAreaAction action = new ChangeAreaAction();
        action.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals(ChangeAreaAction.SCENE_CHANGE)) {
                assert evt.getNewValue() instanceof SceneID;
                SceneID sceneID = (SceneID) evt.getNewValue();
                inputHandler.changeScene(sceneID);
            }
        });
        return action;
    }


    private ButtonGroup setupButtonGroup(List<AbstractButton> buttonList) {
        ButtonGroup buttonGroup = new ButtonGroup();
        for(AbstractButton button : buttonList) {
            buttonGroup.add(button);
        }
        buttonGroup.setSelected(buttonList.get(buttonList.size() - 1).getModel(), true);
        return buttonGroup;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                addPlayer(evt);
                break;
            case ChangeNotifications.REMOVE_PLAYER:
                assert evt.getSource() instanceof ViewHand;
                ViewHand hand = (ViewHand) evt.getOldValue();
                String playerNick = hand.getNickname();
                ListIterator<ChangeAreaPanel> iterator = areaList.listIterator();
                while (iterator.hasNext()){
                    ChangeAreaPanel area = iterator.next();
                    if(area.getAreaName().equalsIgnoreCase(playerNick)) {
                        SwingUtilities.invokeLater(
                                () -> {
                                    remove(area);
                                    revalidate();
                                    repaint();
                                    setVisible(true);
                                }
                        );
                        hand.removePropertyChangeListener(area);
                        buttonPlayerGroup.remove(area.getAreaButton());
                        iterator.remove();
                        return;
                    }
                }
                break;
        }
    }

    private void addPlayer(PropertyChangeEvent evt) {
        assert evt.getSource() instanceof ViewHand;
        ViewHand hand = (ViewHand) evt.getNewValue();
        String playerNick = hand.getNickname();
        SceneID sceneID = inputHandler.isLocalPlayer(playerNick) ?
                SceneID.getMyAreaSceneID() : SceneID.getOpponentAreaSceneID(playerNick);
        // Local player is always considered connected.
        boolean connectionStatus = inputHandler.isLocalPlayer(playerNick) ||
                ((ViewOpponentHand) hand).isConnected();

        ChangeAreaPanel changeAreaPanel = new ChangeAreaPanel(playerNick, buttonAction,
                sceneID, connectionStatus);

        buttonPlayerGroup.add(changeAreaPanel.getAreaButton());
        // Automatically selects the local player
        if (inputHandler.isLocalPlayer(playerNick)) {
            buttonPlayerGroup.setSelected(
                    changeAreaPanel.getAreaButton().getModel(),
                    true
            );
            buttonAction.setOldButton(changeAreaPanel.getAreaButton());
        }
        SwingUtilities.invokeLater(
                () -> {
                    add(changeAreaPanel);
                    revalidate();
                    repaint();
                    setVisible(true);
                }
        );
        areaList.add(changeAreaPanel);
        hand.addPropertyChangeListener(changeAreaPanel);
    }

    /**
     * Adds a button that displays the endgame scene in order for the players to be able
     * to return to the endgame scene.
     * @param text button label
     */
    public void addEndgameButton(String text) {
        SceneID sceneID = SceneID.getEndgameSceneID();
        boolean connectionStatus = true;

        ChangeAreaPanel changeAreaPanel = new ChangeAreaPanel(text, buttonAction,
                sceneID, connectionStatus);

        buttonPlayerGroup.add(changeAreaPanel.getAreaButton());
        SwingUtilities.invokeLater(
                () -> {
                    add(changeAreaPanel);
                    revalidate();
                    repaint();
                    setVisible(true);
                }
        );
        areaList.add(changeAreaPanel);
    }
}
