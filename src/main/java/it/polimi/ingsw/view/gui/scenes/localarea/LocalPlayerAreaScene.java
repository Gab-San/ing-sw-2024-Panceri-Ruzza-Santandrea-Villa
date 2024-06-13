package it.polimi.ingsw.view.gui.scenes.localarea;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.scenes.extra.playerpanel.PlayerListPanel;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import it.polimi.ingsw.view.model.cards.ViewPlaceableCard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.invoke.MethodHandleProxies;
import java.rmi.RemoteException;
import java.util.List;

public class LocalPlayerAreaScene extends JPanel implements GUI_Scene, PropertyChangeListener{
    public static final int WIDTH = GameWindow.SCREEN_WIDTH - PlayerListPanel.WIDTH;
    public static final int HEIGHT = GameWindow.SCREEN_HEIGHT - PlayerInfoPanel.HEIGHT;
    private PlayerInfoPanel playerInfoPanel;
    private PlayAreaPanel playAreaPanel;
    private final GameInputHandler inputHandler;
    private final PlayerHandPanel handPanel;

    public LocalPlayerAreaScene(GameInputHandler inputHandler){
        this.inputHandler = inputHandler;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JLayeredPane layersPane = new JLayeredPane();
        layersPane.setSize(WIDTH, HEIGHT);
        layersPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        playAreaPanel = new PlayAreaPanel();
        //FIXME metti a posto
        JScrollPane scrollPane = new JScrollPane(playAreaPanel);
        
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setBounds(0,0, WIDTH, HEIGHT);
        int horValue = playAreaPanel.getWidth()/2;
        int vertValue = playAreaPanel.getHeight()/2;
        layersPane.add(scrollPane, Integer.valueOf(0));

        BoundedRangeModel vertModel = scrollPane.getVerticalScrollBar().getModel();
        BoundedRangeModel horModel = scrollPane.getHorizontalScrollBar().getModel();

        vertModel.setMaximum(playAreaPanel.getHeight());
        vertModel.setExtent(scrollPane.getHeight());
        System.out.println(vertModel.getExtent());
        vertModel.setValue(vertValue - vertModel.getExtent()/2);
        
        horModel.setMaximum(playAreaPanel.getWidth());
        horModel.setExtent(scrollPane.getWidth());
        System.out.println(horModel.getExtent());
        horModel.setValue(horValue - horModel.getExtent()/2);

        handPanel = new PlayerHandPanel(inputHandler);
        handPanel.setBounds(WIDTH/2 - handPanel.getWidth()/2,
                HEIGHT - handPanel.getHeight(),
                handPanel.getWidth(), handPanel.getHeight());
        layersPane.add(handPanel , Integer.valueOf(10));
        playAreaPanel.setCardListener(handPanel);

        add(layersPane,BorderLayout.CENTER);
    }

    @Override
    public void display() {
        revalidate();
        repaint();
        setVisible(true);
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
        setVisible(false);
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
                            add(playerInfoPanel, BorderLayout.NORTH);
                            revalidate();
                            repaint();
                        }
                );
                localHand
                        .addPropertyChangeListener(playerInfoPanel);
                localHand
                        .addPropertyChangeListener(handPanel);
                ((ViewBoard) evt.getSource())
                        .addPropertyChangeListener(ChangeNotifications.CURRENT_TURN_UPDATE, playerInfoPanel);
                break;
            case ChangeNotifications.ADDED_AREA:
                assert evt.getNewValue() instanceof ViewPlayArea;
                ViewPlayArea playArea = (ViewPlayArea) evt.getNewValue();
                if(!inputHandler.isLocalPlayer(playArea.getOwner())) return;
                playArea.addPropertyChangeListener(ChangeNotifications.VIS_RES_CHANGE, playerInfoPanel);
                playArea.addPropertyChangeListener(playAreaPanel);
                break;
        }
    }
}
