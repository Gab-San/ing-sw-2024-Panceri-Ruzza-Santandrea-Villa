package it.polimi.ingsw.view.gui.scenes.extra;

import it.polimi.ingsw.view.gui.ChatListener;
import it.polimi.ingsw.view.gui.GameInputHandler;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.util.Arrays;

public class ChatPanel extends JPanel implements PropertyChangeListener, ChatListener {
    private final JTextField messageEditArea;
    private final JComboBox<String> addresseeList;
    private final GameInputHandler inputHandler;
    private final ChatDocument chatDocument;
    public ChatPanel(GameInputHandler inputHandler){
        setPreferredSize(new Dimension(300, 250));
        setLayout(new BorderLayout());
        this.inputHandler = inputHandler;


        // Chat viewer section
        // The document is the parallel of the backlog
        // and acts as the model for the text pane
        chatDocument = new ChatDocument();
        chatDocument.addUserStyle("SERVER", Color.BLUE);
        JScrollPane chatVisualizer = setupTextPane(chatDocument);

        // Message sending options this represents
        // the list of connected players that can be reached with messages.

        String[] addresseeOptions = {"ALL"};
        addresseeList = setupStringComboBox(addresseeOptions);
        messageEditArea = setupEditArea(15);
        //Need a panel to layout properly the various components
        JPanel messagingPanel = createMessagingPanel(addresseeList, messageEditArea);

        //Adding components
        add(messagingPanel, BorderLayout.SOUTH);
        add(chatVisualizer, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel createMessagingPanel(JComboBox<String> comboBox, JTextField messageEditArea) {
        JPanel messagingPanel = new JPanel();
        messagingPanel.setLayout(new GridBagLayout());

        addGridComponentToPanel(messagingPanel, messageEditArea, 1, 0, 3, 1, 1.0 ,1.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),
                0, 0);


        addGridComponentToPanel(messagingPanel, comboBox, 0,0,1,1,0.01,0.01,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0)
                ,0,0);
        return messagingPanel;
    }

    private void addGridComponentToPanel(JPanel container, Component component, int x, int y, int width, int height,
                                         double weightx, double weighty, int anchor, int fill,
                                         Insets insets, int ipadx, int ipady){
        container.add(component, new GridBagConstraints(x, y, width, height, weightx, weighty, anchor, fill,
                insets, ipadx, ipady));
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

    private JComboBox<String> setupStringComboBox(String[] addresseeList) {
        JComboBox<String> comboBox = new JComboBox<>(addresseeList);
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(false);
        return comboBox;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }

    @Override
    public void displayMessage(String messenger, String message) {
        String[] composedMessenger = messenger.split(" to ");
        System.out.println(Arrays.toString(composedMessenger));
        String sender = composedMessenger[0];
        String addressee = null;
        try {
            if(composedMessenger.length >= 2){
                addressee = composedMessenger[1];
            }
            chatDocument.insertChatMessage(composedMessenger[0], addressee, message, null);
        } catch (BadLocationException e) {
            e.printStackTrace(System.err);
        }
    }
}
