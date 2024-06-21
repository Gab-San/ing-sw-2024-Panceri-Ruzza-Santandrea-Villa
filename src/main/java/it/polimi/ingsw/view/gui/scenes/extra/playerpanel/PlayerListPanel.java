package it.polimi.ingsw.view.gui.scenes.extra.playerpanel;

import it.polimi.ingsw.view.SceneID;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.gui.scenes.endgame.EndgameScene;
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

public class PlayerListPanel extends JPanel implements PropertyChangeListener{
    public static int WIDTH = 300;
    public static int HEIGHT = 200;
    private final GameInputHandler inputHandler;
    private final ButtonGroup buttonPlayerGroup;
    private final ChangeAreaAction buttonAction;
    private final List<ChangeAreaPanel> areaList;

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
        String playerNick;
        SceneID sceneID;
        boolean connectionStatus;
        ViewHand hand;
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                assert evt.getSource() instanceof ViewHand;
                hand = (ViewHand) evt.getNewValue();
                playerNick = hand.getNickname();
                sceneID = inputHandler.isLocalPlayer(playerNick) ?
                        SceneID.getMyAreaSceneID() : SceneID.getOpponentAreaSceneID(playerNick);
                connectionStatus = inputHandler.isLocalPlayer(playerNick) ||
                        ((ViewOpponentHand) hand).isConnected();
                ChangeAreaPanel changeAreaPanel = new ChangeAreaPanel(playerNick, buttonAction,
                        sceneID, connectionStatus);
                buttonPlayerGroup.add(changeAreaPanel.getAreaButton());
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
                break;
            case ChangeNotifications.REMOVE_PLAYER:
                assert evt.getSource() instanceof ViewHand;
                hand = (ViewHand) evt.getOldValue();
                playerNick = hand.getNickname();
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
