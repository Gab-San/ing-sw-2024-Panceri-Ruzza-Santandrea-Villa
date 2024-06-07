package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.GamePoint;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.Scene;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public abstract class TUI_Scene implements Scene {
    private static final int NOTIF_CHAT_SPACING = 10;
    protected final PrintWriter out;
    protected List<String> notificationBacklog;
    protected List<String> chatBacklog;

    protected TUI_Scene(List<String> notificationBacklog, List<String> chatBacklog) {
        this.out = new PrintWriter(System.out, true);
        this.notificationBacklog = notificationBacklog;
        this.chatBacklog = chatBacklog;
    }
    protected TUI_Scene() {
        this(new LinkedList<>(), new LinkedList<>());
    }

    public final void setNotificationBacklog(List<String> backlog){
        this.notificationBacklog = backlog;
    }
    public final void setChatBacklog(List<String> backlog){
        this.chatBacklog = backlog;
    }

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

    private void displayNoPrompt(){
        cls();
        print();

        int notifMaxMsgLen = notificationBacklog.stream()
                .mapToInt(String::length)
                .max().orElse(0);

        notifMaxMsgLen += NOTIF_CHAT_SPACING;

        int backlogSize = Math.max(notificationBacklog.size(), chatBacklog.size());
        int notifDiff = backlogSize - notificationBacklog.size();
        int chatDiff = backlogSize - chatBacklog.size();
        for (int i = 0; i < backlogSize; i++) {
            if(i >= notifDiff)
                out.print(pad(notificationBacklog.get(i-notifDiff), notifMaxMsgLen));
            if(i >= chatDiff)
                out.print(chatBacklog.get(i-chatDiff));
            out.println();
        }
    }
    private void printCommandPrompt(){
        synchronized(System.out){
            System.out.print("Command > ");
        }
    }

    private String pad(String msg, int len) {
        if(msg.length() < len)
            return msg + " ".repeat(len - msg.length());
        else return msg;
    }

    protected abstract void print();
    @Override
    public void displayError(String error){
        synchronized (System.out){
            displayNoPrompt();
            out.println(RED_TEXT + error + RESET);
            printCommandPrompt();
        }
    }
    @Override
    public final void displayNotification(List<String> backlog){
        synchronized (System.out) {
            this.notificationBacklog = backlog;
            display();
        }
    }
    @Override
    public final void displayChatMessage(List<String> backlog){
        synchronized (System.out) {
            this.chatBacklog = backlog;
            display();
        }
    }
    @Override
    public void moveView(List<CornerDirection> cornerDirections){
        display();
    }
    @Override
    public void setCenter(int row, int col){
        display();
    }
    @Override
    public void setCenter(GamePoint center){
        display();
    }
}