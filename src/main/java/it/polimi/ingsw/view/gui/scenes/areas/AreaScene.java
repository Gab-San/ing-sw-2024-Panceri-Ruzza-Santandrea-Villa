package it.polimi.ingsw.view.gui.scenes.areas;

import it.polimi.ingsw.view.GameColor;
import it.polimi.ingsw.view.gui.*;
import it.polimi.ingsw.view.gui.scenes.extra.playerpanel.PlayerListPanel;
import it.polimi.ingsw.view.model.ViewBoard;
import it.polimi.ingsw.view.model.ViewHand;
import it.polimi.ingsw.view.model.ViewPlayArea;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


/**
 * Base class of the player board, with shared behaviour of local and opponent playAreas.
 * <p>
 *     The player board is comprehensive of: <br>
 *     - player's area: area in which the cards are placed <br>
 *     - player's hand: the set of cards held by the player <br>
 *     - player's info: player's status labels.
 * </p>
 */
public abstract class AreaScene extends JPanel implements GUI_Scene, PropertyChangeListener {
    /**
     * Area scene desired width.
     */
    public static final int WIDTH = GameWindow.SCREEN_WIDTH - PlayerListPanel.WIDTH;
    /**
     * Area scene desired height.
     */
    public static final int HEIGHT = GameWindow.SCREEN_HEIGHT - PlayerInfoPanel.HEIGHT;
    /**
     * Player info panel.
     */
    protected PlayerInfoPanel playerInfoPanel;
    /**
     * Notification displaying label.
     */
    protected final JLabel notificationLabel;
    /**
     * Timer that handles displaying label.
     */
    protected Timer displayTimer;
    /**
     * Scrollable panel that contains the play area.
     */
    protected final JScrollPane scrollPane;
    /**
     * Layered panel in which the player area is displayed.
     */
    protected final JLayeredPane layersPane;
    /**
     * Placed cards display panel.
     */
    protected final AreaPanel areaPanel;
    /**
     * Player's hand display panel.
     */
    protected final HandPanel handPanel;

    /**
     * Constructs the player's board panel.
     * @param areaPanel player's play area display panel
     * @param handPanel player's hand display panel
     */
    protected AreaScene(AreaPanel areaPanel, HandPanel handPanel){
        this.areaPanel = areaPanel;
        this.handPanel = handPanel;

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        layersPane = new JLayeredPane();
//        layersPane.setSize(WIDTH, HEIGHT);
        layersPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        notificationLabel = GUIFunc.createNotificationLabel();
        modifyNotificationLabel();
        layersPane.add(notificationLabel, Integer.valueOf(10000));

        scrollPane = setupAreaScrollPane();
        layersPane.add(scrollPane, Integer.valueOf(0));

        int xHandPanel = WIDTH/2 - handPanel.getWidth()/2;
        handPanel.setBounds(xHandPanel > 0 ? xHandPanel : 20,
                HEIGHT - handPanel.getHeight() - 50,
                handPanel.getWidth(), handPanel.getHeight());
        layersPane.add(handPanel,  Integer.valueOf(10));


        add(layersPane,BorderLayout.CENTER);
    }

    private void modifyNotificationLabel() {
        notificationLabel.setBounds(WIDTH/4,0, WIDTH/2, 200);
        notificationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        notificationLabel.setVerticalAlignment(SwingConstants.CENTER);
        notificationLabel.setFont(new Font("Raleway", Font.PLAIN, 30));
    }


    private JScrollPane setupAreaScrollPane(){
        JScrollPane scrollPane = initializeScrollPane();
        // Center scroll pane
        int horValue = areaPanel.getWidth()/2;
        int vertValue = areaPanel.getHeight()/2;

        BoundedRangeModel vertModel = scrollPane.getVerticalScrollBar().getModel();
        BoundedRangeModel horModel = scrollPane.getHorizontalScrollBar().getModel();

        vertModel.setMaximum(areaPanel.getHeight());
        vertModel.setExtent(scrollPane.getHeight());

        vertModel.setValue(vertValue - vertModel.getExtent()/2);

        horModel.setMaximum(areaPanel.getWidth());
        horModel.setExtent(scrollPane.getWidth());

        horModel.setValue(horValue - horModel.getExtent()/2);

        setScrollPaneMovement(scrollPane);
        scrollPane.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        scrollPane.requestFocusInWindow(); //FIXME: if this is problematic change to grabFocus()
                    }
                }
        );

        return scrollPane;
    }

    @NotNull
    private JScrollPane initializeScrollPane() {
        JScrollPane scrollPane = new JScrollPane(areaPanel,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));
        scrollPane.setSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));

        int currentWidth = GameWindow.getDisplayWindow().getWidth()  - PlayerListPanel.WIDTH;
        int currentHeight = GameWindow.getDisplayWindow().getHeight() - PlayerInfoPanel.HEIGHT;
        scrollPane.setBounds(0,0, currentWidth, currentHeight);
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


    @Override
    public synchronized void display() {
        revalidate();
        repaint();
        setVisible(true);
    }

    @Override
    public synchronized void displayError(String error) {
        int displayTime =  GUIFunc.setupDisplayTimer((float) 1.5, displayTimer);
        notificationLabel.setForeground(GameColor.ERROR_COLOUR.getColor());
        notificationLabel.setText(GUIFunc.correctToLabelFormat(error));
        // The error will become visible
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }

    @Override
    public synchronized void displayNotification(java.util.List<String> backlog) {
        int displayTime = GUIFunc.setupDisplayTimer(2.0f, displayTimer);
        notificationLabel.setForeground(Color.black);
        notificationLabel.setText(GUIFunc.correctToLabelFormat(backlog.get(0)));
        notificationLabel.setVisible(true);
        startDisplayTimer(displayTime);
    }

    @Override
    public synchronized void close() {
        setVisible(false);
    }

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
                    revalidate();
                    repaint();
                }
        );
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


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                assert evt.getNewValue() instanceof ViewHand;
                ViewHand hand = (ViewHand) evt.getNewValue();
                playerInfoPanel = new PlayerInfoPanel(hand.isDeadlocked(), hand.getTurn());
                SwingUtilities.invokeLater(
                        () -> {
                            add(playerInfoPanel, BorderLayout.NORTH);
                            revalidate();
                            repaint();
                        }
                );
                hand.addPropertyChangeListener(playerInfoPanel);
                hand.addPropertyChangeListener(handPanel);
                hand.addPropertyChangeListener(ChangeNotifications.COLOR_CHANGE, areaPanel);
                hand.addPropertyChangeListener(ChangeNotifications.PLAYER_TURN_UPDATE, areaPanel);
                ((ViewBoard) evt.getSource())
                        .addPropertyChangeListener(ChangeNotifications.CURRENT_TURN_UPDATE, playerInfoPanel);
                break;
            case ChangeNotifications.ADDED_AREA:
                assert evt.getNewValue() instanceof ViewPlayArea;
                ViewPlayArea playArea = (ViewPlayArea) evt.getNewValue();
                playArea.addPropertyChangeListener(ChangeNotifications.VIS_RES_CHANGE, playerInfoPanel);
                playArea.addPropertyChangeListener(ChangeNotifications.PLACED_CARD, areaPanel);
                playArea.addPropertyChangeListener(ChangeNotifications.CARD_LAYER_CHANGE, areaPanel);
                break;
        }
    }

    /**
     * Clears the player panel to restore start version.
     */
    public void clear() {
        areaPanel.clear();
    }
}
