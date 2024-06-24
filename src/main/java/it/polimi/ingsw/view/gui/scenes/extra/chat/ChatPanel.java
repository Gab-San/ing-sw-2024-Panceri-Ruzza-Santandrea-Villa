package it.polimi.ingsw.view.gui.scenes.extra.chat;

import it.polimi.ingsw.PlayerColor;
import it.polimi.ingsw.view.gui.ChangeNotifications;
import it.polimi.ingsw.view.gui.ChatListener;
import it.polimi.ingsw.view.gui.GameInputHandler;
import it.polimi.ingsw.view.model.ViewHand;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;

/**
 * This class implements the chat.
 * <p>
 *     It composes of a text field in which to write messages, a sender selector and
 *     a text pane on which chat history is displayed. <br>
 *     When reconnecting chat history is not retained.
 * </p>
 */
public class ChatPanel extends JPanel implements PropertyChangeListener, ChatListener {
    private final JScrollPane chatVisualizer;
    private final JComboBox<String> addresseeList;
    private final GameInputHandler inputHandler;
    private final ChatDocument chatDocument;

    /**
     * Constructs the chat panel.
     * @param inputHandler game input handler
     */
    public ChatPanel(GameInputHandler inputHandler){
        setPreferredSize(new Dimension(300, 250));
        setLayout(new BorderLayout());
        this.inputHandler = inputHandler;


        // Chat viewer section
        // The document is the parallel of the backlog
        // and acts as the model for the text pane
        chatDocument = new ChatDocument();
        chatDocument.addUserStyle("SERVER", new Color(0xad1ae6));
        chatVisualizer = setupTextPane(chatDocument);
        // Message sending options this represents
        // the list of connected players that can be reached with messages.

        MutableComboBoxModel<String> addresseeOptions = new DefaultComboBoxModel<>();
        addresseeOptions.addElement("ALL");
        addresseeList = setupStringComboBox(addresseeOptions);
        JTextField messageEditArea = setupEditArea(15);
        //Need a panel to layout properly the various components
        JPanel messagingPanel = createMessagingPanel(addresseeList, messageEditArea);

        //set not focusable (with tab)
        addresseeList.setFocusable(false);

        //Adding components
        add(messagingPanel, BorderLayout.SOUTH);
        add(chatVisualizer, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createMessagingPanel(JComboBox<String> comboBox, JTextField messageEditArea) {
        JPanel messagingPanel = new JPanel();
        messagingPanel.setLayout(new GridBagLayout());
        messageEditArea.setBorder(BorderFactory.createEtchedBorder());

        addGridComponentToPanel(messagingPanel, messageEditArea, 1, 3, 1.0 ,1.0,
                GridBagConstraints.CENTER, new Insets(0,0,0,0)
        );

        comboBox.setBorder(BorderFactory.createEmptyBorder(5,5,5,10));
        addGridComponentToPanel(messagingPanel, comboBox, 0, 1, 0.01,0.01,
                GridBagConstraints.EAST, new Insets(0,0,0,0)
        );
        return messagingPanel;
    }

    private void addGridComponentToPanel(JPanel container, Component component, int x, int width,
                                         double weightx, double weighty, int anchor,
                                         Insets insets){
        container.add(component, new GridBagConstraints(x, 0, width, 1, weightx, weighty, anchor, GridBagConstraints.HORIZONTAL,
                insets, 0, 0));
    }

    private JTextField setupEditArea(int column) {
        JTextField textField = new JTextField(column);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String selectedAddressee = (String) addresseeList.getSelectedItem();
                    assert selectedAddressee != null;
                    try{
                        inputHandler.sendMsg(selectedAddressee, textField.getText());
                    } catch (RemoteException ex) {
                        inputHandler.notifyDisconnection();
                    }
                    textField.setText("");
                }
            }
        });
        return textField;
    }

    private JScrollPane setupTextPane(ChatDocument document) {
        JTextPane textPane = new JTextPane(document);
        textPane.setEditable(false);
        textPane.setFocusable(false);
        return new JScrollPane(textPane);
    }

    private JComboBox<String> setupStringComboBox(MutableComboBoxModel<String> addresseeList) {
        JComboBox<String> comboBox = new JComboBox<>(addresseeList);
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(false);
        return comboBox;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String playerNickname;
        switch (evt.getPropertyName()){
            case ChangeNotifications.ADDED_PLAYER:
                assert evt.getNewValue() instanceof ViewHand;
                playerNickname = ((ViewHand) evt.getNewValue()).getNickname();
                addresseeList.addItem(playerNickname);
                break;
            case ChangeNotifications.REMOVE_PLAYER:
                assert evt.getOldValue() instanceof ViewHand;
                playerNickname = ((ViewHand) evt.getOldValue()).getNickname();
                addresseeList.removeItem(playerNickname);
                break;
            case ChangeNotifications.COLOR_CHANGE:
                assert evt.getSource() instanceof ViewHand;
                playerNickname = ((ViewHand) evt.getSource()).getNickname();
                assert evt.getNewValue() instanceof PlayerColor;
                Color playerColor = PlayerColor.getColor((PlayerColor) evt.getNewValue());
                chatDocument.addUserStyle(playerNickname, playerColor);
                break;
        }

    }

    @Override
    public void displayMessage(String messenger, String message) {
        String[] composedMessenger = messenger.split(" to ");
        String sender = composedMessenger[0];
        String addressee = null;
        try {
            if(composedMessenger.length >= 2){
                addressee = composedMessenger[1];
            }
            chatDocument.insertChatMessage(sender, addressee, message, null);
        } catch (BadLocationException e) {
            e.printStackTrace(System.err);
        }
        if(isViewAtBottom()){
            scrollToBottom();
        }
    }

    private boolean isViewAtBottom(){
        JScrollBar vertScrollBar = chatVisualizer.getVerticalScrollBar();
        int min = vertScrollBar.getValue() + vertScrollBar.getVisibleAmount();
        int max = vertScrollBar.getMaximum();

        return min >= (max - 2*vertScrollBar.getBlockIncrement());
    }

    private void scrollToBottom(){
        SwingUtilities.invokeLater(
                () -> chatVisualizer.getVerticalScrollBar()
                        .setValue(chatVisualizer.getVerticalScrollBar().getMaximum())
        );
    }
}
