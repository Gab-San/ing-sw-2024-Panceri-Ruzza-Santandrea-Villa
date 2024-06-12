package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.gui.scenes.extra.chat.ChatPanel;
import it.polimi.ingsw.view.gui.scenes.extra.playerpanel.PlayerListPanel;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This is the main frame of the UI. All the scenes are coded to be panels
 * attached to it, and it will control the interactions between the panels.
 */
public class GameWindow extends JFrame implements PropertyChangeListener {
    /**
     * Window starting width.
     */
    public static final int SCREEN_WIDTH = 1280;
    /**
     * Window starting height
     */
    public static final int SCREEN_HEIGHT = 720;
    private final GameInputHandler inputHandler;
    private ChatPanel chatPanel;
    private PlayerListPanel playerListPanel;

    /**
     * Game window constructor.
     * @param inputHandler input handler ref
     */
    public GameWindow(GameInputHandler inputHandler){
        // Standard JFrame stuff defining window size, position and layout
        GUIFunc.setupFrame(this, "Codex Naturalis",
                SCREEN_WIDTH, SCREEN_HEIGHT);
        this.inputHandler = inputHandler;
        setLayout(new BorderLayout());
        // Sets up the panel containing chat and players list
        JPanel leftSidePanel = setupLeftPanel();
        add(leftSidePanel, BorderLayout.WEST);
    }

    private JPanel setupLeftPanel() {
        chatPanel = new ChatPanel(inputHandler);
        inputHandler.addChatListener(chatPanel);

        playerListPanel = new PlayerListPanel(inputHandler);
        inputHandler.addPropertyListener(chatPanel);
        inputHandler.addPropertyListener(playerListPanel);
        // Putting chat panel and players list into a resizable split pane
        // So that each user can choose the dimension they want
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                playerListPanel, chatPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.5);

        JPanel leftPanel = new JPanel(new GridLayout(1,0));
        leftPanel.setPreferredSize(new Dimension(300,10));
        leftPanel.add(splitPane);
        return leftPanel;
    }

    public void displayScene(GUI_Scene nextScene) {
        add((JPanel) nextScene, BorderLayout.CENTER);
        // Sets and executes the scene
        SceneManager.getInstance().setScene(nextScene);
        // This needs to be called each time a scene change
        // is issued to be sure that all the items are validated for display
        setVisible(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(ChangeNotifications.ADDED_PLAYER)){
            // When a player is added than it adds chat and
            // hand as listeners to their event.
            ViewHand hand = (ViewHand) evt.getNewValue();
            hand.addPropertyChangeListener(ChangeNotifications.COLOR_CHANGE, chatPanel);
            hand.addPropertyChangeListener(playerListPanel);
        }
    }
}
