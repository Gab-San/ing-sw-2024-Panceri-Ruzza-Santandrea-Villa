package it.polimi.ingsw.view.gui.scenes.extra;

import javax.swing.text.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ChatDocument extends DefaultStyledDocument {
    private final Map<String, Style> usersStyles;
    private int fontSize;
    public ChatDocument(){
        this(15);
    }

    public ChatDocument(int fontSize){
        usersStyles = new HashMap<>();
        this.fontSize = fontSize;
        try( InputStream fontIS = this.getClass().getClassLoader().getResourceAsStream("fonts/raleway/Raleway-VariableFont_wght.ttf") ){
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            assert fontIS != null;
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontIS));
        } catch (IOException | FontFormatException e) {
            System.err.println(e.getMessage());
        }
    }

    public void addUserStyle(String user, Color color){
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style style = this.addStyle(user, def);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontSize(style, fontSize);
        StyleConstants.setFontFamily(style, "Raleway");
        synchronized (usersStyles){
            usersStyles.put(user.toUpperCase(), style);
        }
    }

    public void insertChatMessage(String messenger, String addressee,
                                  String message, Style a) throws BadLocationException {
        Style nicknameStyle;
        Style def;

        if(a == null) {
            def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        } else  def = a;
        StyleConstants.setFontSize(def, fontSize);
        StyleConstants.setFontFamily(def, "Raleway");

        synchronized (usersStyles){
            Style messengerStyle = usersStyles.get(messenger.toUpperCase());
            nicknameStyle =  messengerStyle != null ? messengerStyle : def;
        }

        insertString(getLength(), messenger, nicknameStyle);

        if(addressee != null) {
            Style sendeeStyle = usersStyles.get(addressee.toUpperCase());
            nicknameStyle =  sendeeStyle != null ? sendeeStyle  : def;
            insertString(getLength(), " to " + addressee, nicknameStyle);
        }

        insertString(getLength(), ": ", nicknameStyle);

        insertString(getLength(), message + "\n", def);
    }


    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
    }

}
