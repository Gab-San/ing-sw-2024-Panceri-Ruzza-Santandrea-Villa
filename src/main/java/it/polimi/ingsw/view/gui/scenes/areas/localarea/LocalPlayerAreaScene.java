package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.view.gui.*;
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

public class LocalPlayerAreaScene extends JPanel implements GUI_Scene, PropertyChangeListener{
    public static final int WIDTH = GameWindow.SCREEN_WIDTH - PlayerListPanel.WIDTH;
    public static final int HEIGHT = GameWindow.SCREEN_HEIGHT - PlayerInfoPanel.HEIGHT;
    private PlayerInfoPanel playerInfoPanel;
    private final PlayAreaPanel playAreaPanel;
    private final GameInputHandler inputHandler;
    private final PlayerHandPanel handPanel;
    private final JLabel notificationLabel;
    private Timer displayTimer;

    public LocalPlayerAreaScene(GameInputHandler inputHandler){
        this.inputHandler = inputHandler;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JLayeredPane layersPane = new JLayeredPane();
        layersPane.setSize(WIDTH, HEIGHT);
        layersPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        notificationLabel = GUIFunc.createNotificationLabel();
        //TODO: [Ale] scale up font size, it's a bit small
        notificationLabel.setBounds(WIDTH/4,0, WIDTH/2, 200);
        notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        layersPane.add(notificationLabel, Integer.valueOf(10000));
        playAreaPanel = new PlayAreaPanel();

        JScrollPane scrollPane = setupAreaScrollPane();
        layersPane.add(scrollPane, Integer.valueOf(0));

        handPanel = new PlayerHandPanel(inputHandler);
        handPanel.setBounds(WIDTH/2 - handPanel.getWidth()/2,
                HEIGHT - handPanel.getHeight(),
                handPanel.getWidth(), handPanel.getHeight());
        layersPane.add(handPanel,  Integer.valueOf(10));

        playAreaPanel.setCornerListener(handPanel);

        add(layersPane,BorderLayout.CENTER);
    }

    public JScrollPane setupAreaScrollPane(){
        JScrollPane scrollPane = new JScrollPane(playAreaPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setSize(new Dimension(WIDTH, HEIGHT));
        int currentWidth = GameWindow.getDisplayWindow().getWidth()  - PlayerListPanel.WIDTH;
        int currentHeight = GameWindow.getDisplayWindow().getHeight() - PlayerInfoPanel.HEIGHT;
        scrollPane.setBounds(0,0, currentWidth, currentHeight);
        int horValue = playAreaPanel.getWidth()/2;
        int vertValue = playAreaPanel.getHeight()/2;

        BoundedRangeModel vertModel = scrollPane.getVerticalScrollBar().getModel();
        BoundedRangeModel horModel = scrollPane.getHorizontalScrollBar().getModel();

        vertModel.setMaximum(playAreaPanel.getHeight());
        vertModel.setExtent(scrollPane.getHeight());

        vertModel.setValue(vertValue - vertModel.getExtent()/2);

        horModel.setMaximum(playAreaPanel.getWidth());
        horModel.setExtent(scrollPane.getWidth());

        horModel.setValue(horValue - horModel.getExtent()/2);
        return scrollPane;
    }

    @Override
    public synchronized void display() {
        revalidate();
        repaint();
        setVisible(true);
    }

    @Override
    public synchronized void displayError(String error) {
        displayError(error, 1.5f, false);
    }

    @Override
    public synchronized void displayNotification(List<String> backlog) {

    }

    @Override
    public synchronized void close() {
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
                playArea.addPropertyChangeListener(ChangeNotifications.PLACED_CARD, playAreaPanel);
                break;
        }
    }

//region ERROR LABEL METHODS
    private void displayError(String errorMessage, float displayTimeSeconds, boolean close) {
        int displayTime =  GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        //TODO [Gamba] Fix color
        notificationLabel.setForeground(Color.red);
        notificationLabel.setText(errorMessage);
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime, close);
    }

    private void startDisplayTimer(int displayTime, boolean isClosing) {
        // After delay time the notification will
        // disappear from the screen
        displayTimer = new Timer(displayTime,
                (event) -> {
                    notificationLabel.setVisible(false);
                    // java.awt timers don't stop after
                    // the delay time has passed,
                    // so they need to be actively stopped
                    displayTimer.stop();
                    displayTimer = null;
                    if(isClosing){
                        close();
                    }
                });
        displayTimer.start();
    }
//endregion
}
