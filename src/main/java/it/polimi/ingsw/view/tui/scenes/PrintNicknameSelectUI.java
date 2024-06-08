package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.tui.TUI_Scene;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

/**
 * The Nickname selection UI scene printer for the TUI
 */
public class PrintNicknameSelectUI extends TUI_Scene {
    @Override
    public void display(){
        synchronized (System.out) {
            cls();
            print();
        }
    }
    @Override
    public void print() {
        out.print("Choose your nickname (min 3 characters): ");
        out.flush();
    }
    @Override
    public void displayError(String msg){
        synchronized (System.out){
            cls();
            out.println(RED_TEXT + msg + RESET);
            print();
        }
    }
}
