package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.CornerDirection;
import it.polimi.ingsw.Point;
import it.polimi.ingsw.view.Client;
import it.polimi.ingsw.view.Scene;

import java.io.PrintWriter;
import java.util.List;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public abstract class TUI_Scene implements Scene {
    protected final PrintWriter out;

    protected TUI_Scene() {
        this.out = new PrintWriter(System.out, true);
    }

    public static void cls(){
        Client.cls();
    }
    @Override
    public final void display(){
        synchronized (System.out){
            cls();
            print();
        }
    }
    protected abstract void print();
    @Override
    public void displayError(String msg){
        synchronized (System.out){
            display();
            out.println(RED_TEXT + msg + RESET);
        }
    }
    @Override
    public void displayNotification(String msg){
        synchronized (System.out) {
            display();
            out.println(msg);
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
    public void setCenter(Point center){
        display();
    }
}