package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.GUI_Scene;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.gui.GameWindow;
import it.polimi.ingsw.view.gui.scenes.extra.playerpanel.PlayerListPanel;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class LocalPlayerAreaScene extends JPanel implements GUI_Scene, PropertyChangeListener {
    private PlayerInfoPanel playerInfoPanel;
    private final GameInputHandler inputHandler;
    public LocalPlayerAreaScene(GameInputHandler inputHandler){
        this.inputHandler = inputHandler;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(GameWindow.SCREEN_WIDTH - PlayerListPanel.WIDTH, GameWindow.SCREEN_HEIGHT));
        setBackground(Color.YELLOW);
    }
    @Override
    public void display() {
        setVisible(true);
        revalidate();
        repaint();
    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotification(List<String> backlog) {

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                assert evt.getNewValue() instanceof ViewHand;
                ViewHand hand = (ViewHand) evt.getNewValue();
                boolean isLocal = inputHandler.isLocalPlayer(hand.getNickname());
                if(!isLocal) return;
                ViewPlayerHand localHand = (ViewPlayerHand) evt.getNewValue();
                playerInfoPanel = new PlayerInfoPanel(localHand.isDeadlocked(), localHand.getTurn());
                SwingUtilities.invokeLater(
                        () -> {
                            add(playerInfoPanel);
                            revalidate();
                            repaint();
                        }
                );
                localHand.addPropertyChangeListener(playerInfoPanel);
                ((ViewBoard) evt.getSource())
                        .addPropertyChangeListener(ChangeNotifications.CURRENT_TURN_UPDATE, playerInfoPanel);
                break;
            case ChangeNotifications.ADDED_AREA:
                assert evt.getNewValue() instanceof ViewPlayArea;
                ViewPlayArea playArea = (ViewPlayArea) evt.getNewValue();
                if(!inputHandler.isLocalPlayer(playArea.getOwner())) return;
                playArea.addPropertyChangeListener(ChangeNotifications.VIS_RES_CHANGE, playerInfoPanel);
        }
    }
}
