package it.polimi.ingsw.view.tui.scenes;

import it.polimi.ingsw.view.tui.TUI_Scene;

import static it.polimi.ingsw.view.tui.ConsoleTextColors.RED_TEXT;
import static it.polimi.ingsw.view.tui.ConsoleTextColors.RESET;

public class PrintNicknameSelectUI extends TUI_Scene {

    public PrintNicknameSelectUI(){
        super();
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
