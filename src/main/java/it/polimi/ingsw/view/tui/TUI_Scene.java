package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.Scene;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.tui.ConsoleColorsCombiner.removeAllANSIColors;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

/**
 * Common parent class of all TUI scenes. <br>
 * Implements all shared behaviours of the scenes such as the backlogs.
 */
public abstract class TUI_Scene implements Scene {
    private static final int NOTIF_CHAT_SPACING = 10;
    protected final PrintWriter out;
    protected List<String> notificationBacklog;
    protected List<String> chatBacklog;

    /**
     * Builds the scene and sets the backlog lists
     */
    protected TUI_Scene(List<String> notificationBacklog, List<String> chatBacklog) {
        this.out = new PrintWriter(System.out, true);
        this.notificationBacklog = notificationBacklog;
        this.chatBacklog = chatBacklog;
    }

    /**
     * Builds the scene with empty list backlogs (to prevent NullPointerExceptions)
     */
    protected TUI_Scene() {
        this(new LinkedList<>(), new LinkedList<>());
    }
    /**
     * Sets the notification backlog as a reference to the given list.
     * All changes to the list are reflected in the scene.
     * @param backlog the View's notification backlog
     */
    public final void setNotificationBacklog(List<String> backlog){
        this.notificationBacklog = backlog;
    }

    /**
     * Sets the chat backlog as a reference to the given list.
     * All changes to the list are reflected in the scene.
     * @param backlog the View's chat backlog
     */
    public final void setChatBacklog(List<String> backlog){
        this.chatBacklog = backlog;
    }

    /**
     * Short alias for Client.cls() <br>
     * Clears the console screen.
     */
    public static void cls(){
        Client.cls();
    }
    @Override
    public void display(){
        synchronized (System.out){
            displayNoPrompt();
            printCommandPrompt();
        }
    }

    /**
     * Prints the whole scene except for the command prompt at the bottom. <br>
     * This partial display is useful to add the error message before the command prompt.
     */
    private void displayNoPrompt(){
        cls();
        print();
        int notifMaxMsgLen;
        synchronized (notificationBacklog){
             notifMaxMsgLen = notificationBacklog.stream()
                    .map(ConsoleColorsCombiner::removeAllANSIColors)
                    .mapToInt(String::length)
                    .max().orElse(0);
        }
        // the "maxMsgLen" is intended to be the "amount of characters displayed"
        // any ANSI code offsets that count because it's an invisible set of characters,
        // so I remove them and only count the number of visible characters

        notifMaxMsgLen += NOTIF_CHAT_SPACING;

        int backlogSize = Math.max(notificationBacklog.size(), chatBacklog.size());
        int notifDiff = backlogSize - notificationBacklog.size();
        int chatDiff = backlogSize - chatBacklog.size();
        for (int i = 0; i < backlogSize; i++) {
            // the inversion "i-xDiff" is needed because I'm printing top->bottom
            // which is oldest->newest which is positions 0->size()-1
            // If one list is shorter, I want the backlog prints
            // to align at the bottom instead of the top.
            if(i >= notifDiff)
                out.print(pad(notificationBacklog.get(i-notifDiff), notifMaxMsgLen));
            if(i >= chatDiff)
                out.print(chatBacklog.get(i-chatDiff));
            out.println();
        }
    }
    /**
     * Prints only the command prompt.
     * To be used after displayNoPrompt() to complete the scene
     */
    private void printCommandPrompt(){
        synchronized(System.out){
            System.out.print("Command > ");
        }
    }

    /**
     * Pads the notification messages to all be the same display length. <br>
     * Display length is calculated by removeAllANSIColors(msg).length()
     * @param msg the notification to pad
     * @param len the desired length
     * @return the msg padded by adding spaces on the right (end of string)
     */
    private String pad(String msg, int len) {
        int msgLength = msg.contains(ConsoleBackgroundColors.PREFIX) ? removeAllANSIColors(msg).length() : msg.length();
        if(msgLength < len)
            return msg + " ".repeat(len - msgLength);
        else return msg;
    }

    /**
     * Print the core of the scene.
     */
    protected abstract void print();
    @Override
    public void displayError(String error){
        synchronized (System.out){
            displayNoPrompt();
            out.println(RED_TEXT + error + RESET);
            printCommandPrompt();
        }
    }


    public final void displayNotification(List<String> backlog){
        synchronized (System.out) {
            this.notificationBacklog = backlog;
            display();
        }
    }

    public void moveView(List<CornerDirection> cornerDirections){
        display();
    }

    public void setCenter(int row, int col){
        display();
    }

    public void setCenter(GamePoint center){
        display();
    }
}