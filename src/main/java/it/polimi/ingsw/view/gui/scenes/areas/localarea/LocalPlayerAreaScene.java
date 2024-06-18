package it.polimi.ingsw.view.gui.scenes.areas.localarea;

import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.scenes.extra.playerpanel.PlayerListPanel;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import it.polimi.ingsw.view.model.ViewPlayerHand;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final JScrollPane scrollPane;
    private final JLayeredPane layersPane;

    public LocalPlayerAreaScene(GameInputHandler inputHandler){
        this.inputHandler = inputHandler;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        layersPane = new JLayeredPane();
//        layersPane.setSize(WIDTH, HEIGHT);
        layersPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        notificationLabel = GUIFunc.createNotificationLabel();
        modifyNotificationLabel();
        layersPane.add(notificationLabel, Integer.valueOf(10000));
        playAreaPanel = new PlayAreaPanel();

        scrollPane = setupAreaScrollPane();
        layersPane.add(scrollPane, Integer.valueOf(0));

        handPanel = new PlayerHandPanel(inputHandler);
        int xHandPanel = WIDTH/2 - handPanel.getWidth()/2;
        handPanel.setBounds(xHandPanel > 0 ? xHandPanel : 20,
                HEIGHT - handPanel.getHeight() - 50,
                handPanel.getWidth(), handPanel.getHeight());
        layersPane.add(handPanel,  Integer.valueOf(10));

        playAreaPanel.setCornerListener(handPanel);

        add(layersPane,BorderLayout.CENTER);
    }

    private void modifyNotificationLabel() {
        notificationLabel.setBounds(WIDTH/4,0, WIDTH/2, 200);
        notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        notificationLabel.setVerticalAlignment(SwingConstants.CENTER);
        notificationLabel.setFont(new Font("Raleway", Font.PLAIN, 30));
    }

    public JScrollPane setupAreaScrollPane(){
        JScrollPane scrollPane = initializeScrollPane();
        // Center scroll pane
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
        setScrollPaneMovement(scrollPane);

        scrollPane.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        scrollPane.grabFocus();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        scrollPane.setFocusable(true);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        scrollPane.setFocusable(false);
                    }
                }
        );

        return scrollPane;
    }

    private void setScrollPaneMovement(JScrollPane scrollPane) {
        // Add WASD movement
        scrollPane.getInputMap().put(KeyStroke.getKeyStroke("D"), "MOVE_RIGHT");
        scrollPane.getActionMap().put("MOVE_RIGHT", new MoveScreenAction(1,0));
        scrollPane.getInputMap().put(KeyStroke.getKeyStroke("A"), "MOVE_LEFT");
        scrollPane.getActionMap().put("MOVE_LEFT", new MoveScreenAction(-1,0));
        scrollPane.getInputMap().put(KeyStroke.getKeyStroke("W"), "MOVE_UP");
        scrollPane.getActionMap().put("MOVE_UP", new MoveScreenAction(0,-1));
        scrollPane.getInputMap().put(KeyStroke.getKeyStroke("S"), "MOVE_DOWN");
        scrollPane.getActionMap().put("MOVE_DOWN", new MoveScreenAction(0,1));
    }

    @NotNull
    private JScrollPane initializeScrollPane() {
        JScrollPane scrollPane = new JScrollPane(playAreaPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));
        scrollPane.setSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));

        int currentWidth = GameWindow.getDisplayWindow().getWidth()  - PlayerListPanel.WIDTH;
        int currentHeight = GameWindow.getDisplayWindow().getHeight() - PlayerInfoPanel.HEIGHT;
        scrollPane.setBounds(0,0, currentWidth, currentHeight);
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
        displayError(error, 1.5f);
    }

    @Override
    public synchronized void displayNotification(List<String> backlog) {
        displayNotification(backlog.get(0), 2f);
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

//region NOTIFICATION LABEL METHODS
    private void displayError(String errorMessage, float displayTimeSeconds) {
        int displayTime =  GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        notificationLabel.setForeground(GameColor.ERROR_COLOUR.getColor());
        notificationLabel.setText(GUIFunc.correctToLabelFormat(errorMessage));
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }

    private void startDisplayTimer(int displayTime) {
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
                });
        displayTimer.start();
    }

    private synchronized void displayNotification(String successMessage, float displayTimeSeconds){
        int displayTime = GUIFunc.setupDisplayTimer(displayTimeSeconds, displayTimer);
        notificationLabel.setForeground(Color.black);
        notificationLabel.setText(GUIFunc.correctToLabelFormat(successMessage));
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }
//endregion


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        SwingUtilities.invokeLater(
                () -> {
                    int currentWidth = GameWindow.getDisplayWindow().getWidth() - PlayerListPanel.WIDTH;
                    int currentHeight = GameWindow.getDisplayWindow().getHeight() - PlayerInfoPanel.HEIGHT;
                    scrollPane.setBounds(0, 0, currentWidth, currentHeight);

                    int xHandPanel = GameWindow.getDisplayWindow().getWidth()/2 - handPanel.getWidth()/2;
                    int yHandPanel = GameWindow.getDisplayWindow().getHeight() - handPanel.getHeight() - 80;
                    handPanel.setBounds(xHandPanel > 0 ? xHandPanel : 10,
                            yHandPanel, handPanel.getWidth(), handPanel.getHeight());
                }
        );
    }
}
