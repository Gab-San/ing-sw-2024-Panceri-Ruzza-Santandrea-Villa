package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.SceneManager;
import it.polimi.ingsw.view.gui.scenes.extra.ChatPanel;
import it.polimi.ingsw.view.gui.scenes.extra.PlayerListPanel;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.instrument.ClassFileTransformer;

public class GameWindow extends JFrame implements PropertyChangeListener {
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    private final GameInputHandler inputHandler;
    private ChatPanel chatPanel;
    private PlayerListPanel playerListPanel;
    public GameWindow(GameInputHandler inputHandler){
        // Standard JFrame stuff defining window size, position and layout
        GUIFunc.setupFrame(this, "Codex Naturalis",
                SCREEN_WIDTH, SCREEN_HEIGHT);
        this.inputHandler = inputHandler;
        setLayout(new BorderLayout());

        JPanel leftSidePanel = setupLeftPanel();
        add(leftSidePanel, BorderLayout.WEST);
    }

    private JPanel setupLeftPanel() {
        chatPanel = new ChatPanel(inputHandler);

        inputHandler.addChatListener(chatPanel);
        playerListPanel = new PlayerListPanel(inputHandler);

        inputHandler.addPropertyListener(chatPanel);
        inputHandler.addPropertyListener(playerListPanel);
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
            ViewHand hand = (ViewHand) evt.getNewValue();
            System.out.println("Added " + hand.getNickname());
            hand.addPropertyChangeListener(chatPanel);
            hand.addPropertyChangeListener(playerListPanel);
        }
    }
}
