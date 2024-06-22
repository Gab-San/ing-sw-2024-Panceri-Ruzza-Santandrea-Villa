package it.polimi.ingsw.view.gui.scenes.extra.chat;

import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the chat model, a document saving the chat history in order to be always displayed.
 * Disconnection does not save chat messages.
 */
public class ChatDocument extends DefaultStyledDocument {
    private final Map<String, Style> usersStyles;
    private final int fontSize;

    /**
     * Default constructor.
     */
    public ChatDocument(){
        this(15);
    }

    /**
     * Constructs a chat document with the desired font.
     * @param fontSize desired font size
     */
    public ChatDocument(int fontSize){
        usersStyles = new HashMap<>();
        this.fontSize = fontSize;
    }

    /**
     * Associated the selected color to the player, in order to display the player's name
     * with their chosen color.
     * @param user player's identifier
     * @param color player associated color
     */
    public void addUserStyle(String user, Color color){
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style style = this.addStyle(user, def);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontSize(style, fontSize);
        StyleConstants.setFontFamily(style, "Inter");
        synchronized (usersStyles){
            usersStyles.put(user.toUpperCase(), style);
        }
    }

    /**
     * Inserts a message into the chat document.
     * @param messenger user who sent the message (eventually the server)
     * @param addressee message addressee
     * @param message message text
     * @param a eventual message style
     * @throws BadLocationException cannot insert message within the document
     */
    public void insertChatMessage(String messenger, String addressee,
                                  String message, Style a) throws BadLocationException {
        Style nicknameStyle;
        Style def;

        if(a == null) {
            def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        } else  def = a;

        StyleConstants.setFontSize(def, fontSize);
        StyleConstants.setFontFamily(def, "Inter");

        synchronized (usersStyles){
            Style messengerStyle = usersStyles.get(messenger.toUpperCase());
            nicknameStyle =  messengerStyle != null ? messengerStyle : def;
        }

        insertString(getLength(), messenger, nicknameStyle);

        if(addressee != null) {

            Style sendeeStyle = usersStyles.get(addressee.toUpperCase());
            nicknameStyle = sendeeStyle != null ? sendeeStyle : def;
            // If sending a private message to self display self.
            String trueSendee = addressee.equals(messenger) ? "SELF" : addressee;
            insertString(getLength(), " to " , def);
            insertString(getLength(),  trueSendee, nicknameStyle);
        }

        insertString(getLength(), ": ", nicknameStyle);

        insertString(getLength(), message + "\n", def);
    }
}
